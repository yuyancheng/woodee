package com.sf.kh.controller;
import com.google.common.collect.Lists;
import java.util.Date;
import com.sf.kh.model.Organization;

import code.ponfee.commons.extract.DataExtractor;
import code.ponfee.commons.extract.DataExtractorBuilder;
import code.ponfee.commons.extract.FileTooBigException;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageRequestParams;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import com.google.common.collect.*;
import com.sf.kh.exception.BusinessException;
import com.sf.kh.model.*;
import com.sf.kh.service.IDepartmentService;
import com.sf.kh.service.IOrganizationLevelService;
import com.sf.kh.service.IOrganizationService;
import com.sf.kh.service.IOrganizationTypeService;
import com.sf.kh.util.Constants;
import com.sf.kh.util.ExcelUtil;
import com.sf.kh.util.WebContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: 01378178
 * @Date: 2018/6/15 17:56
 * @Description:
 */
@RestController
@RequestMapping("department")
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Resource
    private IDepartmentService departmentService;

    @Resource
    private IOrganizationService organizationService;

    @Resource
    private IOrganizationLevelService organizationLevelService;

    @Resource
    private IOrganizationTypeService organizationTypeService;

    ImmutableList<String> fixedHeaders = ImmutableList.of("ID", "单位名称", "组织级别", "月结卡号", "省份", "城市", "区/县","收件地址");

    /**
     * 根据组织id 获取单位
     * params:
     * orgId       组织id
     * pageNum     当前页
     * pageSize    每页大小
     *
     * @return
     */
    @GetMapping(path = "getDeptsWithHierarchicalOrgByOrgId")
    public Result<Page<List<Department>>> getDeptsWithHierarchicalOrgByOrgId(PageRequestParams params) {

        Map<String, Object> queryParams = params.getParams();

        if (queryParams.get("orgId") == null) {
            return Result.failure(ResultCode.BAD_REQUEST, "组织id不能为空");
        }
        if (queryParams.get(Constants.PAGE_NUM) == null) {
            return Result.failure(ResultCode.BAD_REQUEST, "页数不能为空");
        }
        if (queryParams.get(Constants.PAGE_SIZE) == null) {
            return Result.failure(ResultCode.BAD_REQUEST, "每页大小不能为空");
        }

        return Result.success(new Page(departmentService.getDeptsWithHierarchicalOrgByOrgId(queryParams)));
    }


    /**
     * 根据组织类型id 获取单位
     * 前端入参:
     * orgTypeId
     * pageNum
     * pageSize
     *
     * @return
     */
    @GetMapping(path = "getDeptsWithHierarchicalOrgByOrgTypeId")
    public Result<Page<List<Department>>> getDeptsWithHierarchicalOrgByOrgTypeId(PageRequestParams params) {

        Map<String, Object> queryParams = params.getParams();

        if (queryParams.get("orgTypeId") == null) {
            return Result.failure(ResultCode.BAD_REQUEST, "组织id不能为空");
        }
        if (queryParams.get("pageNum") == null) {
            return Result.failure(ResultCode.BAD_REQUEST, "页数不能为空");
        }
        if (queryParams.get("pageSize") == null) {
            return Result.failure(ResultCode.BAD_REQUEST, "每页大小不能为空");
        }

        return Result.success(new Page(departmentService.getDeptsWithHierarchicalOrgByOrgTypeId(params.getParams())));
    }

    @GetMapping(path = "getDeptWithHierarchicalOrgById")
    public Result<Department> getDeptWithHierarchicalOrgById(HttpSession session, @RequestParam(value = "id", required = true) Long id) {
        return Result.success(departmentService.getDeptWithHierarchicalOrgById(id));
    }


    /**
     * 添加单位
     *
     * @param dept
     * @return
     */
    @PostMapping(path = "addDept")
    public Result<Map> addDept(@RequestBody Department dept) {

        Date date = new Date();
        User user = WebContextHolder.currentUser();
        dept.setCreateBy(user == null ? null : user.getId());
        dept.setCreateTm(date);
        dept.setUpdateBy(user == null ? null : user.getId());
        dept.setUpdateTm(date);
        dept.setValid(ValidStatusEnum.VALID.getCode());

        int i = departmentService.addDept(dept);
        if (i != 1) {
            return Result.failure(ResultCode.SERVER_ERROR, "添加失败, 请重试");
        }
        return Result.success(ImmutableMap.of("id", dept.getId()));
    }


    /**
     * 禁用单位
     *
     * @param ids
     * @return
     */
    @PostMapping(path = "disableDept")
    public Result<Void> disableDept(@RequestBody Department dept) {

        if (CollectionUtils.isEmpty(dept.getDeptIds())) {
            return Result.failure(ResultCode.BAD_REQUEST, "部门id不能为空");
        }

        Date date = new Date();
        User user = WebContextHolder.currentUser();

        Department uptDept = new Department();
        dept.setUpdateBy(user == null ? null : user.getId());
        dept.setUpdateTm(date);
        uptDept.setDeptIds(dept.getDeptIds());

        departmentService.batchDisableDept(dept);

        return Result.success();
    }


    /**
     * 更新单位信息
     *
     * @param session
     * @param dept
     * @return
     */
    @PostMapping(path = "updateDept")
    public Result<Void> updateDept(HttpSession session, @RequestBody Department dept) {

        if (dept.getId() == null) {
            return Result.failure(ResultCode.BAD_REQUEST.getCode(), "id不能为空");
        }

        Date date = new Date();
        User user = WebContextHolder.currentUser();
        dept.setUpdateBy(user == null ? null : user.getId());
        dept.setUpdateTm(date);

        int i = departmentService.updateDept(dept);
        if (i != 1) {
            return Result.failure(ResultCode.SERVER_ERROR, "更新失败, 请重试");
        }
        return Result.success();

    }

    /**
     * @param params pageNum
     *               pageSize
     *               orgTypId
     *               OrgId
     *               deptId
     *               deptNameFuzzy
     * @return //TODO OPTIMIZE
     */
    @PostMapping(path = "getDeptByOrgTypeIdDeptNameFuzzy")
    public Result<Page<Department>> getDeptByOrgTypeIdDeptNameFuzzy(PageRequestParams params) {

        Map<String, Object> queryParams = params.getParams();

        if (queryParams.get("pageNum") == null) {
            return Result.failure(ResultCode.BAD_REQUEST, "页数不能为空");
        }
        if (queryParams.get("pageSize") == null) {
            return Result.failure(ResultCode.BAD_REQUEST, "每页大小不能为空");
        }
        if (queryParams.get("orgTypeId") == null) {
            return Result.failure(ResultCode.BAD_REQUEST, "组织类型id不能为空");
        }
        List<Department> depts = departmentService.getDeptByOrgTypeIdDeptNameFuzzy(queryParams);
        return Result.success(new Page<>(CollectionUtils.isEmpty(depts) ? Lists.newArrayList() : depts));
    }


    /**
     * ===============================================================
     */
    @PostMapping(value = "upload")
    public Result<Void> upload(@RequestParam("orgTypeId") Long orgTypeId, @RequestParam("file") MultipartFile file) {

        List<String> errors = new ArrayList<>();
        List<String> headers = getHeaderToExport(orgTypeId);

        Map<Integer, String> headerIdxNameMap = Maps.newHashMap();
        List<String> dynamicOrgLevelHeaders = Lists.newArrayList();
        Map<String, OrganizationLevel> levelNameMap = Maps.newHashMap();
        Map<Long, Department> headQuarterStore = Maps.newHashMap();


        // 行号和导入的记录行数据
        Map<Integer, String[]> indexAddRecordStoreMap = Maps.newHashMap();
        // 行号和组织路径
        Map<Integer, String> indexOrgPathMap = Maps.newHashMap();
        Multimap<Integer, String> levelIndexPathMap =  ArrayListMultimap.create();
        Map<String, String> pathOrgNameMap = Maps.newHashMap();
        Map<String, String> pathAndParent = Maps.newHashMap();
        Map<String, Organization> pathOrgMap = Maps.newHashMap();
        Map<String, Integer> pathLevelIndexMap = Maps.newHashMap();

        User user = WebContextHolder.currentUser();
        Date date = new Date();

        List<Department> deptToUpdate = Lists.newArrayList();
        List<Department> deptToAdd = Lists.newArrayList();

        String separator = "#@#";

        if (headers == null) {
            return Result.failure(ResultCode.BAD_REQUEST, "不存在对应表头");
        }
        try {

            DataExtractor<String[]> extractor = DataExtractorBuilder.newBuilder(
                    file.getInputStream(), file.getOriginalFilename(), file.getContentType(), headers.toArray(new String[headers.size()])
            ).build();

            extractor.extract((index, record) -> {

                List<String> headerList = null;

                if (index == 0) {

                    headerList = Arrays.asList(record).stream().filter(o -> StringUtils.isNotBlank(o)).collect(Collectors.toList());

                    for (int i = 0; i < headerList.size(); i++) {
                        if (StringUtils.isNotBlank(headerList.get(i))) {
                            headerIdxNameMap.put(i, headerList.get(i));
                        }
                    }
                    List<String> orgLevelNameInHeader = buildOrgLevelNameInHeader(fixedHeaders, headerList);
                    levelNameMap.putAll(getOrgLevelNameMap(orgLevelNameInHeader, orgTypeId));
                    dynamicOrgLevelHeaders.addAll(descendingOrgLevel(Lists.newArrayList(levelNameMap.values())));

                } else {

                    String val = record[0];

                    if (StringUtils.isNotBlank(val) && StringUtils.isNumeric(val)) {
                        // 更新的
                        Department uptDept = new Department();
                        uptDept.setId(Long.valueOf(record[0]));
                        uptDept.setDeptName(record[1]);
                        uptDept.setCustNo(record[headers.size() - 5]);
                        uptDept.setProvinceName(record[headers.size() - 4]);
                        uptDept.setCityName(record[headers.size() - 3]);
                        uptDept.setAreaName(record[headers.size() - 2]);
                        uptDept.setAddressDetail(record[headers.size() - 1]);
                        uptDept.setUpdateTm(date);
                        uptDept.setUpdateBy(user == null ? null : user.getId());

                        deptToUpdate.add(uptDept);

                    } else if (StringUtils.isBlank(val)) {
                        // 行 和 导入数据的映射
                        indexAddRecordStoreMap.put(index, record);

                        String path = "";

                        int flexFiledBeginIndex = 3;

                        for (int i = flexFiledBeginIndex; i < dynamicOrgLevelHeaders.size() + flexFiledBeginIndex; i++) {

                            // 跨行校验
                            if (i < dynamicOrgLevelHeaders.size() + flexFiledBeginIndex - 1) {
                                if (StringUtils.isBlank(record[i]) && StringUtils.isNotBlank(record[i + 1])) {
                                    errors.add("第[" + (index+1) + "]行, 导入数据格式错误, 不允许跨组织");
                                    continue;
                                }
                            }

                            if(StringUtils.isNotBlank(record[i])){
                                StringBuilder sb = new StringBuilder();
                                for (int j = flexFiledBeginIndex; j <= i; j++) {
                                    if(StringUtils.isNotBlank(record[j])) {
                                        sb.append(record[j]).append(separator);
                                    }
                                }

                                path = StringUtils.substringBeforeLast(sb.toString(), separator);
                                pathOrgNameMap.put(path, record[i]);

                                Integer levelIndex = pathLevelIndexMap.get(path);
                                if (levelIndex != null && !pathLevelIndexMap.get(path).equals(levelIndex)) {
                                    errors.add("第" + (index+1) + "行错误,[" + record[i] + "]出现在不同级别上");
                                }
                                pathLevelIndexMap.put(path, i-flexFiledBeginIndex + 1);
                                pathAndParent.put(path, path.equals(StringUtils.substringBeforeLast(path, separator)) ? null : StringUtils.substringBeforeLast(path, separator));
                                levelIndexPathMap.put(i - flexFiledBeginIndex + 1, path);
                            }
                        }

                        // 行 和 组织名称路径的映射
                        indexOrgPathMap.put(index, path);

                    } else {
                        // 异常的
                        errors.add("第[" + (index+1) + "]行, ID无效, 添加新数据请将ID清空");
                    }

                }
            });


            if (!indexAddRecordStoreMap.isEmpty()){
                // 校验组织,
                for (int i = 0; i < dynamicOrgLevelHeaders.size(); i++) {

                    List<String> paths = ((ArrayListMultimap<Integer, String>) levelIndexPathMap).get(i + 1);
                    List<String> distinctPaths = distinct(paths);

                    if (CollectionUtils.isNotEmpty(distinctPaths)) {
                        for (String path : distinctPaths) {

                            Organization o = pathOrgMap.get(path);

                            if (o == null) {

                                String name = pathOrgNameMap.get(path);
                                if (i == 0) {
                                    List<Organization> dbOrgs = organizationService.getOrgByNameAndTypeId(name, 0L, orgTypeId);
                                    if (CollectionUtils.isEmpty(dbOrgs)) {
                                        errors.add("该组织层级不存在[" + path.replaceAll(separator, "->") + "]");
                                        continue;
                                    }

                                    pathOrgMap.put(path, dbOrgs.get(0));

                                } else {
                                    String parentPath = pathAndParent.get(path);
                                    Organization parentOrg = pathOrgMap.get(parentPath);
                                    if (parentOrg == null) {
                                        errors.add("组织[" + pathOrgNameMap.get(path) + "]无法找到父级组织对象");
                                        continue;
                                    }

                                    List<Organization> dbOrgs = organizationService.getOrgByNameAndTypeId(name, parentOrg.getId(), orgTypeId);
                                    if (CollectionUtils.isEmpty(dbOrgs)) {
                                        errors.add("不存在[" + path.replaceAll(separator, "->") + "]组织层级");
                                        continue;
                                    }
                                    pathOrgMap.put(path, dbOrgs.get(0));
                                }
                            }

                        }
                    }
                }

            }

            if(CollectionUtils.isNotEmpty(errors)){
                StringBuilder sb = new StringBuilder();
                for(String s : errors){
                    sb.append(s).append("\r\n");
                }
                throw new BusinessException(ResultCode.BAD_REQUEST, sb.toString());
            }



            // 根据 行 和 path 映射， 拿到 path 和组织映射
            // 行path映射 , path组织映射


            for(Map.Entry<Integer, String> entry : indexOrgPathMap.entrySet()){

                Integer lindex = entry.getKey();
                String orgPath = entry.getValue();

                Organization org = pathOrgMap.get(orgPath);
                if(org==null){
                    errors.add("第["+ (lindex+1) +"行], 不在相应组织");
                    continue;
                }else{

                    Department toAdd = new Department();

                    Department hq = getHeadQuarterForOrg(org.getId(), headQuarterStore);


                    String[] storedRecord = indexAddRecordStoreMap.get(lindex);

                    toAdd.setDeptName(storedRecord[1]);
                    toAdd.setParentDeptId(hq.getId());
                    toAdd.setTopDeptId(hq.getTopDeptId());
                    toAdd.setHeadQuarter(Department.HeadQuarterEnum.NO.getCode());

                    toAdd.setOrgId(org.getId());
                    toAdd.setOrgTypeId(orgTypeId);
                    toAdd.setValid(ValidStatusEnum.VALID.getCode());

                    toAdd.setCustNo(storedRecord[headers.size()-5]);
                    toAdd.setProvinceName(storedRecord[headers.size()-4]);
                    toAdd.setCityName(storedRecord[headers.size()-3]);
                    toAdd.setAreaName(storedRecord[headers.size()-2]);
                    toAdd.setAddressDetail(storedRecord[headers.size()-1]);

                    toAdd.setVersion(1);

                    toAdd.setUpdateTm(date);
                    toAdd.setCreateBy(user == null ? null : user.getId());
                    toAdd.setCreateTm(date);
                    toAdd.setUpdateBy(user == null ? null : user.getId());

                    deptToAdd.add(toAdd);
                }

            }



            if (errors.isEmpty()) {
                departmentService.addOrUpdateDeptForImport(deptToAdd, deptToUpdate);
            } else {
                StringBuilder sb = new StringBuilder();
                for(String s : errors){
                    sb.append(s).append("\r\n");
                }
                return Result.failure(ResultCode.BAD_REQUEST, sb.toString());
            }
            return Result.success();

        } catch (FileTooBigException e) {
            logger.error("upload department file too big.", e);
            return Result.failure(ResultCode.BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            logger.error("upload department error.", e);
            return Result.failure(ResultCode.BAD_REQUEST, "文件导入出现错误：" + e.getMessage());
        }
    }

    List<String> distinct(List<String> duplicates){
        if(CollectionUtils.isEmpty(duplicates)){
            return null;
        }
        return duplicates.stream().distinct().collect(Collectors.toList());
    }

    // 根据 orgType, flexFiled,  来获取组织



    @GetMapping(value = "downloadTemplate")
    public void exportDeptTemplate(@RequestParam("orgTypeId") Long orgTypeId, HttpServletRequest request, HttpServletResponse response) {

        try {

            if (orgTypeId == null) {
                logger.warn("exportDeptTemplate orgTypeId must be blank.");
                throw new BusinessException(ResultCode.SERVER_ERROR.getCode(), "orgTypeId 为空");
            }
            OrganizationType orgType = organizationTypeService.getOrgTypeById(orgTypeId);
            List<String> header = getHeaderToExport(orgTypeId);

            ExcelUtil.exportData(orgType.getOrgTypeName(), header.toArray(new String[header.size()]), Lists.newArrayList(), request, response);

        } catch (Exception e) {
            logger.error("export template failed", e);
            throw new BusinessException(ResultCode.SERVER_ERROR.getCode(), "导出模板异常");
        }
    }


    List<Organization> getOrgByIds(List<Long> ids) {
        List<Organization> orgs = organizationService.list(ImmutableMap.of("orgIdList", ids));
        return CollectionUtils.isEmpty(orgs) ? Lists.newArrayList() : orgs;
    }


    public String decorateStr(String str) {
        return "\"" + (StringUtils.isBlank(str) ? "" : (str.indexOf('\"') > -1 ? str.replaceAll("\"", "'") : str)) + "\"";
    }


    @GetMapping(path = "download")
    public void exportData(HttpSession session, HttpServletRequest request,  HttpServletResponse response, @RequestParam("orgTypeId") Long orgTypeId) {

        if (orgTypeId == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "组织类型id不能为空");
        }

        OrganizationType type = organizationTypeService.getOrgTypeById(orgTypeId);
        if (type == null) {
            logger.warn("export department, orgType not exists. orgTypeId={}", orgTypeId);
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "组织类型不存在");
        }
        // 获取header
        List<String> header = getHeaderToExport(orgTypeId);               //TODO OPTIMIZE
        List<String> flexHeader = organizationLevelService.getDescendingOrgLevelName(orgTypeId);

        List<Department> depts = departmentService.getValidDeptsByOrgTypeId(orgTypeId, null, null);


        Map<Long, String> orgNameMap = Maps.newHashMap();
        Map<Long, Organization> orgMap = Maps.newHashMap();


        List<String[]> body = Lists.newArrayList();
        for (Department dept : depts) {

            List<String> row = Lists.newArrayList();

            Long orgId = dept.getOrgId();
            if (orgMap.get(orgId) == null) {
                Organization org = organizationService.getById(orgId);
                if (null != org) {
                    orgMap.put(orgId, org);
                    orgNameMap.put(org.getId(), org.getOrgName());
                } else {
                    logger.warn("export department can not find org, orgId={}", orgId);
                    continue;
                }
            }

            String orgPath = orgMap.get(orgId).getOrgPath();
            List<String> paths = Arrays.asList(orgPath.split(":"));
            List<Long> lpaths = Lists.newArrayList();
            for (String s : paths) {
                lpaths.add(Long.valueOf(s));
            }

            //build org cache
            List<Long> missingOrgs = lpaths.stream().filter(a -> orgMap.get(a) == null).collect(Collectors.toList());

            List<Organization> orgs = getOrgByIds(missingOrgs);

            for (Organization org : orgs) {
                orgMap.put(org.getId(), org);
                orgNameMap.put(org.getId(), org.getOrgName());
            }
            //
            row.add(String.valueOf(dept.getId()));
            row.add(dept.getDeptName());
            row.add(orgMap.get(orgId).getLevelName());

            for (int i = 0; i < flexHeader.size(); i++) {
                if((i+1) > paths.size()){
                    row.add("");
                }else{
                    Organization tmpOrg = orgMap.get(Long.valueOf(paths.get(i)));
                    row.add(tmpOrg==null ? "" : tmpOrg.getOrgName());
                }
            }
            row.add(dept.getCustNo());
            row.add(dept.getProvinceName());
            row.add(dept.getCityName());
            row.add(dept.getAreaName());
            row.add(dept.getAddressDetail());
            body.add(row.toArray(new String[row.size()]));
        }

        String fileName = type.getOrgTypeName() + "单位";
        ExcelUtil.exportData(fileName, header.toArray(new String[header.size()]), body, request, response);

    }

    /**
     * 构建导出数据的表头信息
     *
     * @param orgTypeId
     * @return
     */
    public List<String> getHeaderToExport(Long orgTypeId) {

        List<String> flexHeaders = organizationLevelService.getDescendingOrgLevelName(orgTypeId);

        List<String> mergedHeaders = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(flexHeaders)) {
            mergedHeaders = mergeHeader(fixedHeaders, flexHeaders);
        }

        return mergedHeaders;
    }

    public List<String> mergeHeader(List<String> fixedHeaders, List<String> flexHeaders) {
        List<String> rtnList = Lists.newArrayList();
        rtnList.add(fixedHeaders.get(0));
        rtnList.add(fixedHeaders.get(1));
        rtnList.add(fixedHeaders.get(2));
        for (String str : flexHeaders) {
            rtnList.add(str);
        }

        for (int i = 3; i < fixedHeaders.size(); i++) {
            String tmp = fixedHeaders.get(i);
            rtnList.add(tmp);
        }
        return rtnList;
    }


    public Department getHeadQuarterForOrg(Long orgId, Map<Long, Department> headQuarterStore) {

        Department headQuarter = headQuarterStore.get(orgId);
        if (headQuarter != null) {
            return headQuarter;
        }

        headQuarter = departmentService.getHeadQuarterForOrgId(orgId);
        headQuarterStore.put(orgId, headQuarter);
        return headQuarter;
    }

    public Organization getOrgByOrgLevelIdAndName(Long levelId, String orgName, Map<String, Organization> store) {

        String key = orgName + ":" + levelId;

        Organization org = store.get(key);
        if (org != null) {
            return org;
        }

        org = getOrgByOrgLevelIdAndName(levelId, orgName);
        store.put(key, org);
        return org;
    }

    public Department buildDept(Organization org, Department headQuarter, Map<String, String> contentMap, List<String> fixedHeaders) {

        // need to validate content in advance.
        String deptName = String.valueOf(contentMap.get(fixedHeaders.get(0)));

        String custNo = String.valueOf(contentMap.get(fixedHeaders.get(2)));
        String provinceName = String.valueOf(contentMap.get(fixedHeaders.get(3)));
        String cityName = String.valueOf(contentMap.get(fixedHeaders.get(4)));
        String areaName = String.valueOf(contentMap.get(fixedHeaders.get(5)));
        String addressDetail = String.valueOf(contentMap.get(fixedHeaders.get(6)));

        if (StringUtils.isBlank(deptName)) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "部门名称不能为空");
        }

        User user = WebContextHolder.currentUser();


        Date date = new Date();
        Department dept = new Department();
        dept.setOrgId(org.getId());
        dept.setOrgTypeId(org.getOrgTypeId());
        dept.setValid(ValidStatusEnum.VALID.getCode());
        dept.setDeptName(deptName);
        dept.setParentDeptId(headQuarter.getId());
        dept.setTopDeptId(headQuarter.getTopDeptId());
        dept.setProvinceName(provinceName);
        dept.setCityName(cityName);
        dept.setAreaName(areaName);
        dept.setAddressDetail(addressDetail);
        dept.setCustNo(custNo);
        dept.setHeadQuarter(Department.HeadQuarterEnum.NO.getCode());
        dept.setCreateBy(user == null ? null : user.getId());
        dept.setCreateTm(date);
        dept.setUpdateBy(user == null ? null : user.getId());
        dept.setUpdateTm(date);
//        dept.setDetpPath();
        return dept;
    }


    public Organization getOrgByOrgLevelIdAndName(Long orgLevelId, String orgName) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("levelId", orgLevelId);
        params.put("orgName", orgName);
        params.put("valid", ValidStatusEnum.VALID.getCode());

        List<Organization> orgs = organizationService.list(params);

        if (CollectionUtils.size(orgs) > 1) {
            throw new BusinessException(ResultCode.SERVER_ERROR.getCode(), "同个组织级别, 有都个重名组织");
        }
        if (CollectionUtils.isEmpty(orgs)) {
            throw new BusinessException(ResultCode.SERVER_ERROR.getCode(), "不存在该名字组织[" + orgName + "]");
        }


        Organization org = orgs.get(0);
        if (org == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "不存在该名字组织");
        }

        return org;
    }

    /**
     * 1. 确定orgLevel 对应的 字段名称,
     * 2. 确定字段名称 对应的序号.
     * 3. 确定 orgLevel 是否为最低.
     */


    /**
     * 校验并返回了表头对应的 组织级别
     *
     * @param orgLevelNames
     * @param orgTypeId
     * @return
     */
    public Map<String, OrganizationLevel> getOrgLevelNameMap(List<String> orgLevelNames, Long orgTypeId) {

        List<OrganizationLevel> orgLevel = organizationLevelService.getOrgLevelByOrgTypeId(orgTypeId);

        Map<String, OrganizationLevel> existOrgLevelMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(orgLevel)) {
            for (OrganizationLevel level : orgLevel) {
                existOrgLevelMap.put(level.getLevelName(), level);
            }
        }

        Map<String, OrganizationLevel> rtnLevelMap = Maps.newHashMap();
        for (String str : orgLevelNames) {
            if (StringUtils.isNotBlank(str)) {
                OrganizationLevel level = existOrgLevelMap.get(str);
                if (level == null) {
                    throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "文件格式出错,组织级别[" + str + "]" + "不存在");
                }
                rtnLevelMap.put(str, level);
            }
        }

        return rtnLevelMap;
    }

    /**
     * 获取组织名称, 需要从大到小, 级别逐渐降低
     *
     * @param orgLevel
     * @return
     */
    public List<String> descendingOrgLevel(List<OrganizationLevel> orgLevel) {

        List<String> descendingOrgLevel = Lists.newArrayList();
        OrganizationLevel topOrg = organizationLevelService.buildDescendingOrgLevel(orgLevel);
        OrganizationLevel tmpOrg = topOrg;

        while (tmpOrg != null) {
            descendingOrgLevel.add(tmpOrg.getLevelName());
            tmpOrg = tmpOrg.getSubLevel();
        }

        return descendingOrgLevel;
    }


    public List<String> buildOrgLevelNameInHeader(List<String> fixedHeader, List<String> headerList) {

        List<String> flexHeaders = Lists.newArrayList();

        if (CollectionUtils.isEmpty(fixedHeader)) {
            return headerList;
        }

        for (String tmp : headerList) {
            if (!fixedHeader.contains(tmp)) {
                flexHeaders.add(tmp);
            }
        }
        return flexHeaders;
    }


}
