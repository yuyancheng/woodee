package com.sf.kh.controller;

import code.ponfee.commons.export.ExcelExporter;
import code.ponfee.commons.export.Table;
import code.ponfee.commons.extract.DataExtractor;
import code.ponfee.commons.extract.DataExtractorBuilder;
import code.ponfee.commons.extract.FileTooBigException;
import code.ponfee.commons.io.Files;
import code.ponfee.commons.math.Numbers;
import code.ponfee.commons.model.AbstractDataConverter;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageHandler;
import code.ponfee.commons.model.PageRequestParams;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import code.ponfee.commons.util.Holder;
import code.ponfee.commons.util.RegexUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sf.kh.exception.BusinessException;
import com.sf.kh.model.*;
import com.sf.kh.service.IDepartmentService;
import com.sf.kh.service.IOrganizationLevelService;
import com.sf.kh.service.IOrganizationService;
import com.sf.kh.service.IOrganizationTypeService;
import com.sf.kh.service.IRoleService;
import com.sf.kh.service.IUserService;
import com.sf.kh.startup.SpringStartupListener;
import com.sf.kh.util.CommonUtils;
import com.sf.kh.util.WebContextHolder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * The spring mvc controller for t_user CRUD operators
 * 
 * admin manage user info
 * 
 * @author 01367825
 */
@RestController
@RequestMapping("user/mg")
public class UserManageController {

    private static Logger logger = LoggerFactory.getLogger(UserManageController.class);

    private static final String TITLE_USERNAME = "用户名";
    private static final String TITLE_PURVIEW = "用户角色";
    private static final String TITLE_DEPTNAME = "单位名称";
    private static final String TITLE_PURVSELF = "普通";
    private static final String TITLE_PURVALL = "管理";
    private static final String[] EXPORT_TITLES = { "用户名", "姓名", "手机号码", "组织类型", "用户角色", "单位名称" };

    private @Value("${user.import.max.cloumn.number:30}") int userImportMaxColumnNumber;

    private @Resource IUserService service;
    private @Resource IRoleService roleService;
    private @Resource IOrganizationTypeService orgTypeService;
    private @Resource IDepartmentService deptService;
    private @Resource IOrganizationLevelService orgLevelService;
    private @Resource IOrganizationService orgService;

    @GetMapping("page")
    public Result<Page<Map<String, Object>>> page(PageRequestParams params) {
        if (params.getPageSize() < 1) {
            return Result.failure(ResultCode.BAD_REQUEST, "pageSize不能小于1");
        }
        params.getParams().put("deleted", false);
        return service.query4page(params.getParams());
    }

    @PostMapping("add")
    public Result<Void> add(@RequestBody User user) {
        if (StringUtils.isBlank(user.getUsername())) {
            return Result.failure(ResultCode.BAD_REQUEST, "用户名为空");
        }
        if (!RegexUtils.isValidUserName(user.getUsername())) {
            return Result.failure(ResultCode.BAD_REQUEST, "用户名格式错误");
        }

        userDefaultProp(user, WebContextHolder.currentUser().getId());
        return service.save(user);
    }

    @PostMapping("update")
    public Result<Void> update(@RequestBody User user) {
        if (StringUtils.isNotEmpty(user.getPassword())) {
            // 需要更新密码
            String password = CommonUtils.decryptPassword(user.getPassword());
            if (!RegexUtils.isValidPassword(password)) {
                return Result.failure(ResultCode.BAD_REQUEST, "密码格式错误");
            }
            user.setPassword(CommonUtils.cryptPassword(password));
        }

        user.setRoleIds(Arrays.asList(SpringStartupListener.roleGeneral())); // XXX 默认普通角色
        user.setUpdateBy(WebContextHolder.currentUser().getId());
        return service.update(user);
    }

    @PostMapping("changestatus")
    public Result<Void> changeStatus(@RequestBody Map<String, Object> params) {
        long userId = Numbers.toLong(params.get("userId"));
        int status = Numbers.toInt(params.get("status"));
        return service.changeStatus(userId, WebContextHolder.currentUser().getId(), status);
    }

    @PostMapping("resetpwd")
    public Result<Void> resetpwd(@RequestBody Map<String, Object> params) {
        long userId = Numbers.toLong(params.get("userId"));
        Preconditions.checkArgument(userId > 0, "用户ID不能小于0");
        String userName = (String) params.get("username");
        Preconditions.checkArgument(StringUtils.isNotBlank(userName), "用户名不能为空");

        User user = new User();
        user.setId(userId);
        user.setPassword(CommonUtils.cryptPassword(userName));
        user.setUpdateBy(WebContextHolder.currentUser().getId());
        return service.modifyInfo(user);
    }

    @PostMapping("delete")
    public Result<Void> delete(@RequestBody long[] userIds) {
        return service.logicDelete(userIds, WebContextHolder.currentUser().getId());
    }

    @PostMapping("updatepwd")
    public Result<Void> updatepwd(@RequestBody Map<String, Object> params) {
        long userId = Numbers.toLong(params.get("userId"));
        String password = (String) params.get("password");
        if (StringUtils.isBlank(password)) {
            return Result.failure(ResultCode.BAD_REQUEST, "密码不能为空");
        }

        password = CommonUtils.decryptPassword(password);
        if (!RegexUtils.isValidPassword(password)) {
            return Result.failure(ResultCode.BAD_REQUEST, "密码名格式错误");
        }

        User user = new User();
        user.setId(userId);
        user.setPassword(CommonUtils.cryptPassword(password));
        user.setUpdateBy(WebContextHolder.currentUser().getId());
        return service.modifyInfo(user);
    }

    @PostMapping("updaterole")
    public Result<Void> updateRole(@RequestBody Map<String, Object> params) {
        long userId = Numbers.toLong(params.get("userId"));
        long roleId = Numbers.toInt(params.get("roleId"));
        return service.updateRoles(userId, new long[] {roleId});
    }

    @GetMapping("userrole")
    public Result<Role> userrole(@RequestParam("userId") long userId) {
        return AbstractDataConverter.convertResultBean(
           service.queryUserRoles(userId), 
           roles -> CollectionUtils.isEmpty(roles) ? null : roles.get(0)
       );
    }

    @GetMapping("/template")
    public ResponseEntity<byte[]> template(@RequestParam("orgTypeId") long orgTypeId) {
        OrganizationLevel orgLevel = orgLevelService.getHierarchicalOrgLevel(orgTypeId);
        HttpHeaders headers = new HttpHeaders();
        if (orgLevel == null) {
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<>(
                ("组织类型编号不存在：" + orgTypeId).getBytes(), headers, HttpStatus.CREATED
            );
        }

        List<String> titles = Lists.newArrayList(TITLE_USERNAME, TITLE_PURVIEW);
        String fileName = "用户批量导入模板";
        List<String> list = Lists.newArrayList();
        orgLevelHierarchical(list, orgLevel);
        titles.addAll(list);
        titles.add(TITLE_DEPTNAME);

        Table table = new Table(titles.toArray(new String[titles.size()]));
        // example
        List<Object[]> tbody = Lists.newArrayList();
        String[] row = new String[titles.size()];
        row[0] = "username1";
        row[1] = TITLE_PURVSELF;
        tbody.add(row);
        row = new String[titles.size()];
        row[0] = "username2";
        row[1] = TITLE_PURVALL;
        tbody.add(row);
        table.setTobdy(tbody);

        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData(
            "attachment", new String(fileName.getBytes(UTF_8), ISO_8859_1) + ".xlsx"
        );
        try (ExcelExporter excel = new ExcelExporter()) {
            excel.setName(fileName).build(table);
            return new ResponseEntity<>(excel.export(), headers, HttpStatus.CREATED);
        }
    }

    /**
     * 批量导入用户
     * @param file
     * @return
     * @throws IOException 
     */
    @PostMapping(value = "import")
    public Result<Void> upload(@RequestParam("orgTypeId") long orgTypeId, 
                               @RequestParam("file") MultipartFile file) {
        // 单位验证
        OrganizationLevel orgLevel = orgLevelService.getHierarchicalOrgLevel(orgTypeId);
        if (orgLevel == null) {
            return Result.failure(ResultCode.BAD_REQUEST, "组织类型ID不存在：" + orgTypeId);
        }

        String[] headers = new String[userImportMaxColumnNumber];
        for (int i = 0; i < userImportMaxColumnNumber; i++) {
            headers[i] = i + "";
        }

        try {
            DataExtractor<String[]> extractor = DataExtractorBuilder.newBuilder(
                file.getInputStream(), file.getOriginalFilename(), file.getContentType(), headers
            ).build();

            long createBy = WebContextHolder.currentUser().getId();
            List<User> users = new ArrayList<>();
            List<String> errors = new ArrayList<>();

            Holder<Integer> lastOrgLevelNamePos = Holder.empty();
            List<String> orgLevelNames = new ArrayList<>();
            Map<String, List<String>> orgLevelOrgNameMapping = new HashMap<>();
            Set<String> unique = Sets.newHashSet();
            Map<String, Object> rstRs = Maps.newHashMap();


            extractor.extract((rowNumber, record) -> {
                for (int i = 0; i < record.length; i++) {
                    record[i] = record[i].trim();
                }
                // 首行处理：列标题匹配
                if (rowNumber == 0) {
                    if (!TITLE_USERNAME.equals(record[0]) || !TITLE_PURVIEW.equals(record[1])) {
                        throw new IllegalArgumentException("列标题错误：[" + record[0] + "," + record[1]
                        + "]，第一列标题必须为“" + TITLE_USERNAME + "”，第二列标题必须为“" + TITLE_PURVIEW + "”");
                    }

                    // find the title：“单位名称”
                    for (int i = 2; i < userImportMaxColumnNumber; i++) {
                        if (StringUtils.isBlank(record[i])) {
                            throw new IllegalArgumentException("列标题错误：标题不能为空");
                        } else if (TITLE_DEPTNAME.equals(record[i])) {
                            lastOrgLevelNamePos.set(i - 1);
                            break; // found it
                        } else {
                            orgLevelNames.add(record[i]);
                        }
                    }
                    if (lastOrgLevelNamePos.isEmpty()) {
                        throw new IllegalArgumentException("列标题错误：未找到名为“" + TITLE_DEPTNAME + "”的列标题");
                    } else if (orgLevelNames.isEmpty()) {
                        throw new IllegalArgumentException("列标题错误：未填写组织层级");
                    }
                    List<String> orgLevelList = Lists.newArrayList();
                    orgLevelHierarchical(orgLevelList, orgLevel);
                    if (!ListUtils.isEqualList(orgLevelNames, orgLevelList)) {
                        throw new IllegalArgumentException("组织层级错误：" + orgLevelNames.toString() + " ");
                    }

                    for (String orgLevelName : orgLevelNames) {
                        List<String> orgNames = orgService.listOrgNameByOrgLevelName(orgTypeId, orgLevelName);
                        if (CollectionUtils.isEmpty(orgNames)) {
                            throw new IllegalArgumentException("组织层级下没有对应的组织：" + orgLevelName + " ");
                        }
                        orgLevelOrgNameMapping.put(orgLevelName, orgNames);
                    }

                    return; // 第一行列标题处理完成
                }

                // rowNumber > 0
                String start = "第" + (rowNumber + 1) + "行：";
                String error = start;

                // 1、单位权限校验
                Integer deptPurview = null;
                if (TITLE_PURVSELF.equals(record[1])) {
                    deptPurview = UserDept.PURVIEW_SELF;
                } else if (TITLE_PURVALL.equals(record[1])) {
                    deptPurview = UserDept.PURVIEW_ALL;
                } else {
                    error += TITLE_PURVIEW + "[" + record[1] + "]填写错误，可选的值为[" 
                           + TITLE_PURVSELF + "，" + TITLE_PURVALL + "]；";
                }

                // 2、账户名校验
                if (StringUtils.isBlank(record[0])) {
                    error += TITLE_USERNAME + "不能为空；";
                } else if (!unique.add(record[0])) {
                    error += TITLE_USERNAME + "已重复[" + record[0] + "]；";
                } else if (service.checkUsernameIsExists(record[0]).getData()) {
                    error += TITLE_USERNAME + "已存在[" + record[0] + "]；";
                }
                if(error.lastIndexOf("；")>=0){
                	error = error.substring(0, error.length()-1);
                }
                // 3、组织校验：从后面找第一个不为空的组织名称
                int pos = lastOrgLevelNamePos.get();
                List<String> orgNameList = new ArrayList<>();
                for (int i = 2; i <= pos; i++) {
                    orgNameList.add(record[i]); // orgName
                }

                StringBuilder sb = new StringBuilder();
                for(String s : orgNameList){
                    sb.append(s).append("#@");
                }
                sb.append(record[pos+1]);
                String deptPath = sb.toString();

                Object rst = rstRs.get(deptPath);
                if(rst == null){
                    rst = verifyOrg(orgTypeId, orgNameList, orgLevelNames, record[pos + 1], orgLevelOrgNameMapping);
                    if (rst instanceof String) {
                        error += rst;
                    }
                    rstRs.put(deptPath, rst);
                }


                if (!start.equals(error)) {
                    errors.add(error);
                } else if (errors.isEmpty()) { // 没有错误
                    User user = new User();
                    user.setUsername(record[0]);
                    user.setDeptId((Long) rst);
                    user.setDeptPurview(deptPurview);
                    userDefaultProp(user, createBy);
                    users.add(user);
                }
            });

            if (errors.isEmpty()) {
                return service.batchSave(users);
            } else {
                String fail = StringUtils.join(errors, Files.WINDOWS_LINE_SEPARATOR);
                return Result.failure(ResultCode.BAD_REQUEST, fail);
            }
        } catch (IllegalArgumentException | FileTooBigException | BusinessException e) {
            return Result.failure(ResultCode.BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            logger.info("upload occur exception", e);
            return Result.failure(ResultCode.BAD_REQUEST, "文件导入出现错误：" + e.getMessage());
        }
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export(PageRequestParams params) {
        params.getParams().put("deleted", false);
        params.getParams().putAll(PageHandler.QUERY_ALL_PARAMS);
        Result<Page<Map<String, Object>>> result = service.query4page(params.getParams());
        HttpHeaders headers = new HttpHeaders();
        if (!result.isSuccess()) {
            headers.setContentType(MediaType.TEXT_PLAIN);
            /*WebUtils.response(WebContextHolder.getResponse(), MediaType.TEXT_PLAIN_VALUE, 
                              "导出数据出错：" + result.getMsg(), Files.UTF_8);*/
            return new ResponseEntity<>(("导出数据出错：" + result.getMsg()).getBytes(),
                                         headers, HttpStatus.CREATED);
        }

        Page<Map<String, Object>> page = result.getData();
        List<Object[]> tbody = Lists.newArrayListWithCapacity((int) page.getTotal());
        page.process(map -> {
            int num = Numbers.toInt(map.get("deptPurview"), -1);
            String deptPurview = (num == UserDept.PURVIEW_ALL) ? TITLE_PURVALL 
                                : num == UserDept.PURVIEW_SELF ? TITLE_PURVSELF : "";
            tbody.add(new Object[] {
                map.get("username"), map.get("nickname"),
                map.get("mobilePhone"), map.get("orgTypeName"),
                deptPurview, map.get("deptName"),
            });
        });

        Table table = new Table(EXPORT_TITLES);
        table.setTobdy(tbody);
        String fileName = "用户数据导出";
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData(
            "attachment", new String(fileName.getBytes(UTF_8), ISO_8859_1) + ".xlsx"
        );
        try (ExcelExporter excel = new ExcelExporter()) {
            excel.setName(fileName).build(table);
            return new ResponseEntity<>(excel.export(), headers, HttpStatus.CREATED);
        }
    }

    private void orgLevelHierarchical(List<String> list, OrganizationLevel level) {
        if (level == null) {
            return;
        }
        list.add(level.getLevelName());
        orgLevelHierarchical(list, level.getSubLevel());
    }

    private void userDefaultProp(User user, long createBy) {
        user.setStatus(User.STATUS_ENABLE);
        user.setDeleted(false);
        user.setRoleIds(Arrays.asList(SpringStartupListener.roleGeneral())); // XXX 默认普通角色
        user.setPassword(CommonUtils.cryptPassword(user.getUsername()));
        user.setCreateBy(createBy);
        user.setUpdateBy(createBy);
    }

    private Object verifyOrg(Long orgTypeId, List<String> orgNameList, List<String> orgLevelNames,
                             String deptName, Map<String, List<String>> orgLevelOrgNameMapping) {
        if (StringUtils.isBlank(deptName)) {
            return TITLE_DEPTNAME + "名称不能为空：" + deptName;
        }
        int lastOrgNamePos = -1, i;
        // 从后往前找第一个不为空的组织名称
        for (i = orgNameList.size() - 1; i >= 0; i--) {
            if (StringUtils.isNotBlank(orgNameList.get(i))) {
                lastOrgNamePos = i;
                break;
            }
        }
        if (lastOrgNamePos == -1) {
            return "组织名称列表不能全为空";
        }

        // 1、组织与组织层级匹配校验
        for (i = lastOrgNamePos; i >= 0; i--) {
            String orgName = orgNameList.get(i);
            if (StringUtils.isBlank(orgName)) {
                return "组织名称列表出现断层（中间有为空的组织名称）：" + orgNameList.toString();
            }
            String orgLevelName = orgLevelNames.get(i);
            if (!orgLevelOrgNameMapping.get(orgLevelName).contains(orgName)) {
                return "组织层级[" + orgLevelName + "]不存在组织[：" + orgName + "]";
            }
        }

        // 2、单位与组织校验
        List<Department> depts = deptService.getDepartmentByOrgNameAndDeptName(orgTypeId, orgNameList.get(lastOrgNamePos), deptName);

        if(CollectionUtils.isEmpty(depts)) {
            return TITLE_DEPTNAME + "不存在：" + deptName;
        }

        if(depts.size()==1){
            return depts.get(0).getId();

        }else{

            StringBuilder sb = new StringBuilder();
            String separator = "@#";
            for(String s : orgNameList){
                if(StringUtils.isNotBlank(s)){
                    sb.append(s).append(separator);
                }
            }

            String path = StringUtils.substringBeforeLast(sb.toString(), separator);

            for(Department d : depts){

                Department hierarhicalDept = deptService.getDeptWithHierarchicalOrgById(d.getId());

                if(hierarhicalDept!=null){

                    String toMatchPath = "";

                    Organization org = hierarhicalDept.getOrganization();
                    if(org!=null){
                        toMatchPath = separator+org.getOrgName();
                    }

                    while(org.getParentOrganization()!=null){
                        org = org.getParentOrganization();
                        toMatchPath = separator+org.getOrgName()+toMatchPath;
                    }

                    toMatchPath = StringUtils.substringAfter(toMatchPath, separator);
                    if(path.equals(toMatchPath)){
                        return d.getId();
                    }
                }
            }
        }
        return TITLE_DEPTNAME + "不存在：" + deptName;
    }
}
