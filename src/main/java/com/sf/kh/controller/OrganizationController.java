package com.sf.kh.controller;

import code.ponfee.commons.extract.DataExtractor;
import code.ponfee.commons.extract.DataExtractorBuilder;
import code.ponfee.commons.extract.FileTooBigException;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import com.google.common.collect.*;
import com.sf.kh.exception.BusinessException;
import com.sf.kh.model.*;
import com.sf.kh.service.IOrganizationLevelService;
import com.sf.kh.service.IOrganizationService;
import com.sf.kh.util.ExcelUtil;
import com.sf.kh.util.WebContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: 01378178
 * @Date: 2018/6/15 15:22
 * @Description:
 */
@RestController
@RequestMapping("organization")
public class OrganizationController {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);

    @Resource
    private IOrganizationService organizationService;


    @GetMapping(path = "getAscendingOrgById")
    public Result<Organization> getAscendingOrgById(@RequestParam(value = "id", required = true) Long id) {
        return Result.success(organizationService.getAscendingHierarchicalOrgById(id));
    }


    @PostMapping(path = "addOrg")
    public Result<Map> addOrg(@RequestBody Organization org) {

        Date date = new Date();
        User user = WebContextHolder.currentUser();
        org.setValid(ValidStatusEnum.VALID.getCode());
        org.setCreateBy(user == null ? null : user.getId());
        org.setCreateTm(date);
        org.setUpdateBy(user == null ? null : user.getId());
        org.setUpdateTm(date);

        try {
            int rs = organizationService.addOrg(org);
            if (rs != 1) {
                return Result.failure(ResultCode.SERVER_ERROR, "更新失败, 请重试");
            }
            return Result.success(ImmutableMap.of("id", org.getId(), "levelId", org.getLevelId()));
        } catch (BusinessException be) {
            return Result.failure(be.getCode(), be.getMessage());
        }
    }

    @PostMapping(path = "disableOrg")
    public Result<Void> disableOrg(@RequestBody Map<String, Object> params) {

        Long id = Long.valueOf((int) (params.get("orgId")));
        if (id == null) {
            return Result.failure(ResultCode.BAD_REQUEST, "orgId不能为空");
        }

        Date date = new Date();
        User user = WebContextHolder.currentUser();

        Organization updateOrg = new Organization();

        updateOrg.setId(id);
        updateOrg.setValid(ValidStatusEnum.INVALID.getCode());
        updateOrg.setUpdateBy(user == null ? null : user.getId());
        updateOrg.setUpdateTm(date);


        int rs = organizationService.disableOrg(updateOrg);

        if (rs != 1) {
            return Result.failure(ResultCode.SERVER_ERROR, "更新失败, 请重试");
        }

        return Result.success();


    }

    /**
     * @param org 更新组织 名称, 序号
     * @return
     */
    @PostMapping(path = "updateOrg")
    public Result<Void> updateOrg(@RequestBody Organization org) {


        Date date = new Date();
        User user = WebContextHolder.currentUser();
        org.setUpdateBy(user == null ? null : user.getId());
        org.setUpdateTm(date);


        int i = organizationService.updateOrg(org);

        if (i != 1) {
            return Result.failure(ResultCode.SERVER_ERROR, "更新失败, 请重试");
        }

        return Result.success();

    }

    @GetMapping(path = "getSubOrgsByOrgId")
    public Result<List<Organization>> getSubOrgsByOrgId(@RequestParam(value = "orgId", required = true) Long orgId) {

        List<Organization> orgs = organizationService.getSubOrgs(orgId);
        return Result.success(orgs);

    }

    @GetMapping(path = "getTopOrgsByOrgTypeId")
    public Result<List<Organization>> getTopOrgByOrgTypeId(@RequestParam(value = "orgTypeId", required = true) Long orgTypeId) {
        try {
            List<Organization> orgs = organizationService.getValidTopOrgsByOrgTypeId(orgTypeId);
            return Result.success(orgs);
        } catch (BusinessException be) {
            return Result.failure(be.getCode(), be.getMessage());
        }
    }


    @GetMapping(path = "getValidSubOrgsByOrgTypeId")
    public Result<List<Organization>> getValidSubOrgsByOrgTypeId(@RequestParam(value = "orgTypeId", required = true) Long orgTypeId) {

        try {
            List<Organization> orgs = organizationService.getValidOrgsByOrgTypeId(orgTypeId);
            return Result.success(orgs);

        } catch (BusinessException be) {
            return Result.failure(be.getCode(), be.getMessage());
        }

    }


    @GetMapping(path = "getTopOrgsWithMaterialMark")
    public Result<List<Organization>> getTopOrgsWithMaterialMark() {
        List<Organization> orgs = organizationService.getTopOrgsWithMaterialMark();
        return Result.success(orgs);
    }

    @Resource
    private IOrganizationLevelService organizationLevelService;

    @GetMapping(value = "downloadDesignateOrg")
    public void exportDesignatedOrgTemplate(@RequestParam("orgTypeId") Long orgTypeId, HttpServletRequest request, HttpServletResponse response) {

        if (orgTypeId == null) {
            logger.warn("exportDeptTemplate orgTypeId must be blank.");
            throw new BusinessException(ResultCode.SERVER_ERROR.getCode(), "orgTypeId 为空");
        }

        List<String> orgLevelNames = organizationLevelService.getDescendingOrgLevelName(orgTypeId);

        if (CollectionUtils.isEmpty(orgLevelNames)) {
            throw new BusinessException(ResultCode.SERVER_ERROR.getCode(), "不存在组织级别");
        }

        try {
            ExcelUtil.exportData("组织模板", orgLevelNames.toArray(new String[orgLevelNames.size()]), Lists.newArrayList(), request, response);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.SERVER_ERROR.getCode(), "导出模板异常");
        }
    }


    @Resource
    TransactionTemplate template;

    @PostMapping(value = "upload")
    public Result<Void> upload(@RequestParam("orgTypeId") Long orgTypeId, @RequestParam("file") MultipartFile file) {

        List<String> orgLevelNames = organizationLevelService.getDescendingOrgLevelName(orgTypeId);
        Map<String, OrganizationLevel> levelNameMap = Maps.newHashMap();
        Map<Integer, OrganizationLevel> levelIndexLevelMap = Maps.newHashMap();
        Map<String, Integer> levelNameLevelIndex = Maps.newHashMap();

        Map<String, Organization> pathOrgMap = Maps.newHashMap();
        Map<String, String> pathOrgNameMap = Maps.newHashMap();
        Map<String, Integer> pathLevelIndexMap = Maps.newHashMap();
        Map<String, String> pathAndParent = Maps.newHashMap();
        Multimap<Integer, String> levelIndexPathMap = ArrayListMultimap.create();


        int levelSize = orgLevelNames.size();

        String separator = "&#@@#&";


        if(CollectionUtils.isEmpty(orgLevelNames)){
            throw new BusinessException(ResultCode.BAD_REQUEST, "组织级别不存在");
        }

        try {

            DataExtractor<String[]> extractor = DataExtractorBuilder.newBuilder(
                    file.getInputStream(), file.getOriginalFilename(), file.getContentType(), orgLevelNames.toArray(new String[orgLevelNames.size()])
            ).build();

            List<String> errors = Lists.newArrayList();
            extractor.extract((index, record) -> {


                List<String> headerList = null;

                if(index == 0){
                    headerList = Arrays.asList(record).stream().filter(o -> StringUtils.isNotBlank(o)).collect(Collectors.toList());
                    // validate whether header match to orgLevelNames
                    if(headerList.size() != levelSize){
                        throw new BusinessException(ResultCode.BAD_REQUEST, "导入的表头与组织级别层级不一致");
                    }

                    for(int i=0; i<headerList.size(); i++){
                        if(!headerList.get(i).equals(orgLevelNames.get(i))){
                            throw new BusinessException(ResultCode.BAD_REQUEST, "导入的表头与组织级别层级不一致, 系统中["+orgLevelNames.get(i)+"]导入的头为["+headerList.get(i)+"]");
                        }
                    }

                    List<OrganizationLevel> levels = organizationLevelService.getOrgLevelByOrgTypeId(orgTypeId);

                    for(OrganizationLevel level : levels){
                        levelNameMap.put(level.getLevelName(), level);
                    }

                    for(int i=0; i<headerList.size(); i++){
                        levelIndexLevelMap.put(i+1, levelNameMap.get(headerList.get(i)));
                        levelNameLevelIndex.put(headerList.get(i), i+1);
                    }

                }else{

                    for(int i=0; i<record.length; i++){

                        if( i< record.length - 1){
                            String curVal = record[i];
                            String child = record[i+1];
                            if (StringUtils.isBlank(curVal) && StringUtils.isNotBlank(child)) {
                                errors.add("第" + index + "行错误,不允许跨组织层级添加组织");
                            }
                        }

                        if(StringUtils.isNotBlank(record[i])){

                            StringBuilder sb = new StringBuilder();
                            for(int j=0; j<=i; j++){
                                sb.append(record[j]).append(separator);
                            }

                            String path = StringUtils.substringBeforeLast(sb.toString(), separator);

                            pathOrgNameMap.put( path, record[i] );

                            Integer levelIndex = pathLevelIndexMap.get(path);
                            if(levelIndex!=null && !pathLevelIndexMap.get(path).equals(i+1)) {
                                errors.add("第" + index + "行错误,[" + record[i] + "]出现在不同级别上");
//                                continue;
                            }

                            pathLevelIndexMap.put( path, i+1 );
                            pathAndParent.put( path,  path.equals( StringUtils.substringBeforeLast(path, separator) )? null : StringUtils.substringBeforeLast(path, separator) );
                            levelIndexPathMap.put(i+1, path);
                        }
                    }

                }
            });

            if(CollectionUtils.isNotEmpty(errors)){
                StringBuilder sb = new StringBuilder();
                for(String s : errors){
                    sb.append(s).append("\r\n");
                }
                throw new BusinessException(ResultCode.BAD_REQUEST, sb.toString());
            }

            Date d = new Date();
            User u = WebContextHolder.currentUser();
            template.execute(var ->{

                for(int i=0; i<levelSize; i++){

                    List<String> paths = ((ArrayListMultimap<Integer, String>) levelIndexPathMap).get(i+1);
                    List<String> distinctPaths = distinct(paths);

                    if(CollectionUtils.isNotEmpty(distinctPaths)) {

                        for (String path : distinctPaths) {

                            Organization o = pathOrgMap.get(path);

                            if( o == null) {
                                String name = pathOrgNameMap.get(path);
                                if (i == 0) {
                                    List<Organization> dbOrgs = organizationService.getOrgByNameAndTypeId(name, 0L, orgTypeId);

                                    if(CollectionUtils.isEmpty(dbOrgs)){
                                        Organization insertOrg = new Organization();
                                        insertOrg.setOrgName(name);
                                        insertOrg.setParentOrgId(0L);
                                        insertOrg.setOrgTypeId(orgTypeId);
                                        insertOrg.setLevelName(levelIndexLevelMap.get(i + 1).getLevelName());
                                        insertOrg.setCreateBy(u == null ? null : u.getId());
                                        insertOrg.setUpdateBy(u == null ? null : u.getId());
                                        insertOrg.setSequence(1);
                                        insertOrg.setCreateTm(d);
                                        insertOrg.setUpdateTm(d);
                                        organizationService.addOrg(insertOrg);
                                        Organization addedOrg = organizationService.getById(insertOrg.getId());
                                        pathOrgMap.put(path, addedOrg);
                                    }else{
                                        if(!levelIndexLevelMap.get(i+1).getLevelName().equals(dbOrgs.get(0).getLevelName())){
                                            throw new BusinessException(ResultCode.BAD_REQUEST, "组织["+dbOrgs.get(0).getOrgName()+"]应属于级别["+ dbOrgs.get(0).getLevelName()+"],导入级别为["+levelIndexLevelMap.get(i+1).getLevelName()+"]");
//                                            error.add(dbOrgs.get(0).getOrgName()+"]应属于级别["+ dbOrgs.get(0).getLevelName()+"],导入级别为["+levelIndexLevelMap.get(i+1).getLevelName()+"]");
                                        }
                                        pathOrgMap.put(path, dbOrgs.get(0));
                                    }
                                }else{

                                    String parentPath = pathAndParent.get(path);
                                    Organization parentOrg = pathOrgMap.get(parentPath);
                                    if(parentOrg == null){
                                        throw new BusinessException(ResultCode.BAD_REQUEST, "组织["+pathOrgNameMap.get(path)+"]无法找到父级组织对象");
                                    }

                                    List<Organization> dbOrgs = organizationService.getOrgByNameAndTypeId(name, parentOrg.getId(), orgTypeId);
                                    Organization dbOrg = null;
                                    if(CollectionUtils.isNotEmpty(dbOrgs)){
                                        dbOrg = dbOrgs.get(0);
                                    }

                                    if(dbOrg==null) {

                                        Organization insert = new Organization();
                                        insert.setOrgName(name);
                                        insert.setParentOrgId(parentOrg.getId());
                                        insert.setOrgTypeId(orgTypeId);
                                        insert.setSequence(1);
                                        insert.setLevelName(levelIndexLevelMap.get(i + 1).getLevelName());
                                        insert.setCreateBy(u == null ? null : u.getId());
                                        insert.setUpdateBy(u == null ? null : u.getId());
                                        insert.setCreateTm(d);
                                        insert.setUpdateTm(d);

                                        organizationService.addOrg(insert);
                                        Organization addedOrg = organizationService.getById(insert.getId());
                                        pathOrgMap.put(path, addedOrg);
                                    }else{
                                        if(!levelIndexLevelMap.get(i+1).getLevelName().equals(dbOrg.getLevelName())){
                                            throw new BusinessException(ResultCode.BAD_REQUEST, "组织["+dbOrg.getOrgName()+"]应属于级别["+ dbOrg.getLevelName()+"],导入级别为["+levelIndexLevelMap.get(i+1).getLevelName()+"],不符.");
                                        }
                                        pathOrgMap.put(path, dbOrg);
                                    }
                                }
                            }

                        }

                    }
                }
                return null;
            });

            return Result.success();

        }catch (FileTooBigException e) {
            logger.error("upload organization file too big.", e);
            return Result.failure(ResultCode.BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            logger.error("upload organization error.", e);
            return Result.failure(ResultCode.BAD_REQUEST, "文件导入出现错误：" + e.getMessage());
        }

    }

    List<String> distinct(List<String> duplicates){
        if(CollectionUtils.isEmpty(duplicates)){
            return null;
        }
        return duplicates.stream().distinct().collect(Collectors.toList());
    }
}
