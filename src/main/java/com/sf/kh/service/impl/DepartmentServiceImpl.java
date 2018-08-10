package com.sf.kh.service.impl;
import java.util.Date;

import code.ponfee.commons.constrain.Constraint;
import code.ponfee.commons.constrain.Constraints;
import code.ponfee.commons.log.LogAnnotation;
import code.ponfee.commons.model.PageHandler;
import code.ponfee.commons.model.ResultCode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sf.kh.exception.BusinessException;
import com.sf.kh.dao.IDepartmentDao;
import com.sf.kh.model.*;
import com.sf.kh.service.IDepartmentService;
import com.sf.kh.service.IOrganizationService;
import com.sf.kh.service.IOrganizationTypeService;
import com.sf.kh.service.IUserService;
import com.sf.kh.util.Constants;
import com.sf.kh.util.ConverterUtil;
import com.sf.kh.util.WebContextHolder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Auther: 01378178
 * @Date: 2018/6/19 16:22
 * @Description:
 */
@Service
public class DepartmentServiceImpl implements IDepartmentService {

    @Resource
    private IDepartmentDao departmentDao;

    @Resource
    private IOrganizationService organizationService;

    @Resource
    private IOrganizationTypeService organizationTypeService;

    @Resource
    TransactionTemplate transactionTemplate;

    @Resource
    IUserService userService;

    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);


    @LogAnnotation(type = LogAnnotation.LogType.QUERY)
    @Constraints(@Constraint(field = "orgId", notNull = true))
    @Override
    public List<Department> getValidDeptsByOrgId(Map<String, Object> params) {
        params.put(Constants.VALID, ValidStatusEnum.VALID.getCode());
        PageHandler.NORMAL.handle(params);
        return departmentDao.query4page(params);
    }

    @LogAnnotation(type = LogAnnotation.LogType.QUERY)
    @Override
    public List<Department> getValidDeptsByOrgTypeId(Long orgTypeId, Integer pageNum, Integer pageSize) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("orgTypeId", orgTypeId);
        params.put(Constants.PAGE_NUM, pageNum);
        params.put(Constants.PAGE_SIZE, pageSize);
        params.put(Constants.VALID, ValidStatusEnum.VALID.getCode());
        PageHandler.NORMAL.handle(params);
        return departmentDao.query4page(params);
    }

    @LogAnnotation(type = LogAnnotation.LogType.QUERY)
    @Constraints(@Constraint(field = "orgTypeId", notNull = true))
    @Override
    public List<Department> getDeptsWithHierarchicalOrgByOrgTypeId(Map<String, Object> params) {
        params.put(Constants.VALID, ValidStatusEnum.VALID.getCode());
        PageHandler.NORMAL.handle(params);
        return buildHierarchicalDepts(departmentDao.query4page(params));
    }


    @LogAnnotation(type = LogAnnotation.LogType.QUERY)
    @Constraints(@Constraint(field = "orgId",  notNull = true))
    @Override
    public List<Department> getDeptsWithHierarchicalOrgByOrgId(Map<String, Object> params) {
        params.put(Constants.VALID,  ValidStatusEnum.VALID.getCode());
        PageHandler.NORMAL.handle(params);
        return buildHierarchicalDepts(departmentDao.query4page(params));
    }

    @LogAnnotation(type = LogAnnotation.LogType.QUERY)
    public Department getDeptWithHierarchicalOrgById(Long id){
        Department dept = departmentDao.getById(id);
        if(dept!=null){
            return buildHierarchicalDepts(Lists.newArrayList(dept)).get(0);
        }
        return null;
    }


    public List<Department> buildHierarchicalDepts(List<Department> depts){

        //1. get all organization and orgLevel
        if(CollectionUtils.isEmpty(depts)){
            return depts;
        }

        List<Long> orgIds = depts.stream().map(Department::getOrgId).distinct().collect(Collectors.toList());
        List<Long> orgTypeIds = depts.stream().map(Department::getOrgTypeId).distinct().collect(Collectors.toList());
        List<Organization> deptOrgs = organizationService.getOrgByIds(orgIds);
        List<OrganizationType> orgTypes = organizationTypeService.getValidOrgTypedByIds(orgTypeIds);

        if(CollectionUtils.isNotEmpty(deptOrgs)){

            List<Long> relevantOrgIds = deptOrgs.stream().map(Organization::splitOrgPath).flatMap(o->o.stream()).distinct().collect(Collectors.toList());

            if(CollectionUtils.isEmpty(relevantOrgIds)){
                return depts;
            }

            List<Organization> relevantOrgs = organizationService.getOrgByIds(relevantOrgIds);
            Map<Long, Organization> orgMap = organizationService.buildHierarchicalOrgs(relevantOrgs);
            for(Department dept : depts){
                dept.setOrganization(orgMap.get(dept.getOrgId()));
            }

            // 为物资统计的 东南西北 区域划分而组装. BEGIN
            if(CollectionUtils.isNotEmpty(orgTypes)){
                Map<Long, OrganizationType> orgTypeMap = orgTypes.stream().collect(Collectors.toMap(OrganizationType::getId, Function.identity()));

                for(Department dept : depts){
                    OrganizationType type = orgTypeMap.get(dept.getOrgTypeId());

                    if(type!=null){
                        Integer biRelType = type.getBiRelType()==null ? 1 : type.getBiRelType();
                        if(biRelType==2){
                            OrganizationType t = orgTypeMap.get(dept.getOrgTypeId());
                            dept.setBiRelName(t!=null ? t.getOrgTypeName(): null);
                        }else if(biRelType == 3 && dept.getOrganization()!=null){
                            Organization topOrg = orgMap.get(dept.getOrganization().getTopOrgId());
                            dept.setBiRelName(topOrg!=null?topOrg.getOrgName() : null);
                        }
                    }
                }
            }
            // END
        }

        return depts;
    }



    @LogAnnotation(type = LogAnnotation.LogType.QUERY)
    @Override
    public List<Department> getSubDepts(Long deptId) {

        Department query = new Department();
        query.setParentDeptId(deptId);
        query.setValid(ValidStatusEnum.VALID.getCode());

        List<Department> depts = departmentDao.query4page(ConverterUtil.convertToMap(query));
        if(CollectionUtils.isEmpty(depts)){
            return Lists.newArrayList();
        }

        return depts;
    }

    @Override
    public List<Department> getAllChildDepts(Long deptId) {
        if(deptId==null){
            return Lists.newArrayList();
        }

        List<Department> tmpDetps = this.getSubDepts(deptId);
        List<Long> deptToQuery = Lists.newArrayList();
        List<Department> deptReturn = Lists.newArrayList();

        while(CollectionUtils.isNotEmpty(tmpDetps)){
            for(Department dept : tmpDetps){
                if(!dept.getId().equals(dept.getParentDeptId())) {
                    deptToQuery.add(dept.getId());
                }
            }

            deptReturn.addAll(tmpDetps);

            Map<String, Object> params = Maps.newHashMap();
            params.put("valid",ValidStatusEnum.VALID.getCode());
            params.put("parentDeptIdList",deptToQuery);

            tmpDetps = departmentDao.query4page(params);
            deptToQuery.clear();
        }

        return deptReturn;
    }


    @LogAnnotation(type = LogAnnotation.LogType.UPDATE)
    @Constraint(field = "id", notNull = true)
    @Override
    public int disableDept(Department dept, boolean checkHQ) {

        Department dbDept = getById(dept.getId());
        if(dbDept==null){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "部门不存在");
        }

        Department query = new Department();
        query.setParentDeptId(dept.getId());
        query.setValid(ValidStatusEnum.VALID.getCode());

        List<Department> children = departmentDao.query4page(ConverterUtil.convertToMap(query));
        if(CollectionUtils.isNotEmpty(children)){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), dbDept.getDeptName()+"存在有效下级部门, 不允许删除");
        }

        if(checkHQ && dbDept.getHeadQuarter()!=null && dbDept.getHeadQuarter().equals(Department.HeadQuarterEnum.YES.getCode())){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "总部不允许直接删除, 请到[组织架构]直接删除组织");
        }

        int count = userService.getDeptUserCount(dept.getId());
        if(count>0){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "有关联用户,不允许直接删除");
        }

        Date date = new Date();
        User user = WebContextHolder.currentUser();

        dept.setValid(ValidStatusEnum.INVALID.getCode());
        dept.setUpdateBy(user==null ? null : user.getId());
        dept.setUpdateTm(date);

        return departmentDao.updateDepartment(dept);
    }


    @Override
    public int batchDisableDept(Department dept){

        List<Long> parentDepts = dept.getDeptIds();
        Collections.reverse(parentDepts);

        return transactionTemplate.execute(o->{
                    int rs = 0;
                    for(Long deptId : parentDepts){
                        Department uptDept = new Department();
                        uptDept.setId(deptId);
                        rs += disableDept(uptDept, true);
                    }
                    return rs;
                }
        );
    }


    @Override
    public int addDepts(final List<Department> depts) {

        return transactionTemplate.execute( var -> {
            int i = 0;
            for(Department dept : depts){
                i+=addDept(dept);
            }
            return i;
        });
    }

    @Override
    public int addOrUpdateDeptForImport(List<Department> addDepts, List<Department> uptDepts) {
        return transactionTemplate.execute( var -> {
            int i = 0;
            for(Department dept : addDepts){
                i+=addDept(dept);
            }

            for(Department dept : uptDepts){
                i+=updateSpecificDept(dept);
            }

            return i;
        });
    }

    @Override
    public Department getById(Long id) {
        if(id == null){
            return null;
        }
        return departmentDao.getById(id);
    }

    @Override
    public List<Department> getDeptsByCustNoList(List<String> custNoList) {
        List<Department> depts = null;
        if(CollectionUtils.isNotEmpty(custNoList)) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("custNoList", custNoList);
            depts = this.list(params);
        }
        return CollectionUtils.isEmpty(depts) ? Lists.newArrayList() : depts;
    }



    @Override
    public List<Department> getDeptByOrgTypeIdDeptNameFuzzy(Map<String, Object> params) {

        String deptNameFuzzy = (String) params.get("deptName");
        Long id =  params.get("id")!=null ?  Long.valueOf((String)params.get("id")) : null;
        Long orgId = params.get("orgId")!=null ? Long.valueOf((String) params.get("orgId")) : null ;
        Long orgTypeId = params.get("orgTypeId") != null  ? Long.valueOf((String) params.get("orgTypeId")): null;
        Integer pageSize = (Integer) params.get("pageSize");
        Integer pageNum = (Integer) params.get("pageNum");


        if(StringUtils.isBlank(deptNameFuzzy) && id==null && orgId !=null ){

            List<Department> depts = this.getAllDeptsByOrgId(orgId);
            if(CollectionUtils.isEmpty(depts)){
                return Lists.newArrayList();
            }

            Integer begin = (pageNum -1)<0 ? 0 : (pageNum-1) * pageSize;
            Integer end = begin + pageSize;

            if(begin >= depts.size()){
                return Lists.newArrayList();
            }

            List<Department> deptSubList = null;

            if(end > depts.size()){
                deptSubList = depts.subList(begin, depts.size());
            }else{
                deptSubList = depts.subList(begin, end.intValue());
            }

            return buildHierarchicalDepts(deptSubList);
        }else{

            Map<String, Object> query = Maps.newHashMap();
            query.put("id", id);
            query.put("orgId", orgId);
            query.put("orgTypeId", orgTypeId);
            query.put("deptNameFuzzy", deptNameFuzzy);
            query.put("pageSize", pageSize);
            query.put("pageNum", pageNum);
            query.put("valid", ValidStatusEnum.VALID.getCode());

            PageHandler.NORMAL.handle(query);
            List<Department> depts = departmentDao.query4page(query);
            return buildHierarchicalDepts(depts);
        }

    }

    @Override
    public List<Department> getAllDeptsByOrgId(Long orgId){
        List<Department> rs = Lists.newArrayList();
        if(orgId!=null){
            Department hq = this.getHeadQuarterForOrgId(orgId);
            if(hq!=null && hq.getValid().equals(ValidStatusEnum.VALID.getCode())) {
                List<Department> children = this.getAllChildDepts(hq.getId());
                if (CollectionUtils.isNotEmpty(children)) {
                    rs.addAll(children);
                }
                rs.add(hq);
            }
        }
        return rs;
    }


    @LogAnnotation(type = LogAnnotation.LogType.UPDATE)
    @Constraint(field = "id", notNull = true)
    @Constraint(field="addressDetail", notNull = false, maxLen = 255, msg = "地址长度不能超过255")
    @Constraint(field="deptName", notNull = false, maxLen = 50, msg="名称不能超过50个字符")
    @Override
    public int updateDept(Department dept) {

        Long deptId = dept.getId();
        String deptName = dept.getDeptName();

        // check whether department exists
        Department existedDept = departmentDao.getById(deptId);
        if(existedDept==null){
            logger.warn("update department but not corresponding dept exist, deptId={}.", deptId);
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "部门不存在");
        }

        // 改变名称
        if(StringUtils.isNotBlank(deptName)) {
            List<Department> sameNameDepts = getValidDeptByOrgIdAndDeptName(existedDept.getOrgId(), deptName);
            if (CollectionUtils.isNotEmpty(sameNameDepts) && (sameNameDepts.size() > 1 || !sameNameDepts.get(0).getId().equals(deptId))) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "同一组织下，已存在同名部门["+deptName+"]");
            }
        }


        return departmentDao.updateDepartment(dept);
    }


    public int updateSpecificDept(Department dept){

        if(dept.getId()!=null) {

            Department updDept = new Department();
            updDept.setId(dept.getId());
            updDept.setDeptName(dept.getDeptName());
            updDept.setCustNo(dept.getCustNo());

            updDept.setProvinceName(dept.getProvinceName());
            updDept.setCityName(dept.getCityName());
            updDept.setAreaName(dept.getAreaName());
            updDept.setAddressDetail(dept.getAddressDetail());

            updDept.setUpdateBy(dept.getUpdateBy());
            updDept.setUpdateTm(dept.getUpdateTm());

            return updateDept(updDept);

        }
        logger.warn("update HeadQuarter for data import, update dept failed dept={}", dept);
        throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "更新单位失败["+ dept.getDeptName()+"]"+"id=["+dept.getId()+"]");
    }

    /**
     * 查询同一组织下， 同名的部门
     * @param orgId
     * @param deptName
     * @return
     */
    private List<Department> getValidDeptByOrgIdAndDeptName(Long orgId, String deptName)  {
        Department queryDept = new Department();
        queryDept.setOrgId(orgId);
        queryDept.setDeptName(deptName);
        queryDept.setValid(ValidStatusEnum.VALID.getCode());

        Map<String, Object> params = ConverterUtil.convertToMap(queryDept);
        return departmentDao.query4page(params);
    }

    @LogAnnotation(type = LogAnnotation.LogType.ADD)
    @Constraint(field = "deptName", notNull = true)
    @Constraint(field = "orgId", notNull = true)
    @Override
    public int addDept(Department dept) {

        // check whether headquarter exists for such org
        Long orgId = dept.getOrgId();
        String deptName = dept.getDeptName();

        Department headQuarter = getHeadQuarterForOrgId(orgId);
        if(headQuarter==null){
            logger.warn("org doesn't have corresponding headquarter, orgId={}", orgId);
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "组织下不存在总部");
        }

        // check whether depart name exists in this org
        List<Department> sameNameDepts = getValidDeptByOrgIdAndDeptName(orgId, deptName);
        if(CollectionUtils.isNotEmpty(sameNameDepts)){
            logger.warn("department name already exists in this org, deptName={}, orgId={}", deptName, orgId);
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "同一组织下，已存在同名部门["+deptName+"]");
        }

        // populate department
        dept.setParentDeptId(headQuarter.getId());
        dept.setTopDeptId(headQuarter.getTopDeptId());
        dept.setValid(ValidStatusEnum.VALID.getCode());
        dept.setOrgTypeId(headQuarter.getOrgTypeId());
        dept.setHeadQuarter(Department.HeadQuarterEnum.NO.getCode());
        dept.setVersion(1);

         int i = departmentDao.insertDepartment(dept);
         if(i!=1){
             throw new BusinessException(ResultCode.SERVER_ERROR.getCode(), "添加单位失败");
         }

         Department updDept = new Department();
         updDept.setId(dept.getId());
         updDept.setDeptPath(headQuarter.getDeptPath()+ Constants.PATH_SEPARATOR+dept.getId());
         return departmentDao.updateDepartment(updDept);
    }

    /**
     * 默认情况下, 需要添加的部门组织 已经存在 一个总部的 部门.
     * 为组织添加 总部
     * @param org
     */
    public int addHeadQuarterForOrg(Organization org){

        // getParentOrgHeadCounter
        // 如果 parentOrgId == null || parentOrgId == 0 则代表是

//        Organization ascendingOrg = organizationService.getAscendingHierarchicalOrgById(org.getId());
//        String deptName = null;
//        deptName = ascendingOrg.getOrgName();
//
//        Organization tmpOrg = ascendingOrg;
//        while(tmpOrg.getParentOrganization()!=null ){
//            tmpOrg = tmpOrg.getParentOrganization();
//            deptName = (tmpOrg.getOrgName())+deptName;
//        }


        Department dept = new Department();

        dept.setOrgId(org.getId());
        dept.setDeptName(org.getOrgName()+"总部");
        dept.setHeadQuarter(Department.HeadQuarterEnum.YES.getCode());
        dept.setParentDeptId(0L);
        dept.setOrgTypeId(org.getOrgTypeId());
        dept.setVersion(1);
        dept.setValid(ValidStatusEnum.VALID.getCode());
        dept.setCreateBy(org.getCreateBy());
        dept.setUpdateBy(org.getUpdateBy());
        dept.setUpdateTm(org.getUpdateTm());
        dept.setCreateTm(org.getCreateTm());

        if(org.getParentOrgId() != null && org.getParentOrgId() != 0){

            Department parentDept = getHeadQuarterForOrgId(org.getParentOrgId());

            if(parentDept!=null){
                dept.setParentDeptId(parentDept.getId());
                dept.setTopDeptId(parentDept.getTopDeptId());
            }

            int i = departmentDao.insertDepartment(dept);
            if(i!=1){
                throw new BusinessException(ResultCode.SERVER_ERROR.getCode(), "添加单位失败");
            }

            Department uptDept = new Department();
            uptDept.setId(dept.getId());
            uptDept.setDeptPath((parentDept == null ? null : parentDept.getDeptPath())+Constants.PATH_SEPARATOR+dept.getId());

            return departmentDao.updateDepartment(uptDept);

        }else{

            int i= departmentDao.insertDepartment(dept);

            if(i!=1){
                throw new BusinessException(ResultCode.SERVER_ERROR.getCode(), "添加单位失败");
            }

            Department uptDept = new Department();
            uptDept.setTopDeptId(dept.getId());
            uptDept.setId(dept.getId());
            uptDept.setDeptPath(String.valueOf(dept.getId()));

            return departmentDao.updateDepartment(uptDept);
        }

    }

    @Override
    public Department getHeadQuarterForOrgId(Long orgId){

        Department queryDept = new Department();
        queryDept.setOrgId(orgId);
        queryDept.setHeadQuarter(Department.HeadQuarterEnum.YES.getCode());
        queryDept.setValid(ValidStatusEnum.VALID.getCode());

        List<Department> headQuarter = departmentDao.query4page(ConverterUtil.convertToMap(queryDept));

        if(CollectionUtils.isEmpty(headQuarter)){
            return null;
        }else if(headQuarter.size()>1){
            logger.warn("multiple head quarters exists, orgId={}", orgId);
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "该组织下存在多个总部");
        }

        return headQuarter.get(0);

    }

    @Override
    public List<Department> list(Map<String, ?> params) {
        return departmentDao.query4page(params);
    }


    @Override
    public List<Department> getDepartmentByOrgNameAndDeptName(Long orgTypeId, String orgName, String deptName){
        if(orgTypeId==null){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "组织类型Id不能为空");
        }
        if(StringUtils.isBlank(orgName)){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "组织名称不能为空");
        }
        if(StringUtils.isBlank(deptName)){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "部门名称不能为空");
        }

        Map<String, Object> params = Maps.newHashMap();
        params.put("orgTypeId", orgTypeId);
        params.put("orgName", orgName);
        params.put("deptName", deptName);
        List<Department> depts = departmentDao.selectByOrgNameDeptName(params);
        return depts;
    }


    @Override
    public List<Long> getDeptIdsAccordingly(Long orgTypeId, Long orgId, Long deptId) {

        if(deptId!=null){
            return Lists.newArrayList(deptId);
        }

        if(orgId!=null){
            List<Department> depts = Lists.newArrayList();
            Department hq = getHeadQuarterForOrgId(orgId);
            if(hq!=null) {
                List<Department> children = getAllChildDepts(hq.getId());
                if (CollectionUtils.isNotEmpty(children)) {
                    depts.addAll(children);
                }
                depts.add(hq);
                return depts.stream().map(Department::getId).collect(Collectors.toList());
            }
        }

        if(orgTypeId!=null){
            List<Department> tmp = getValidDeptsByOrgTypeId(orgTypeId, null, null);
            return CollectionUtils.isEmpty(tmp) ? Lists.newArrayList() : tmp.stream().map(Department::getId).collect(Collectors.toList());
        }

        return Lists.newArrayList();
    }

}
