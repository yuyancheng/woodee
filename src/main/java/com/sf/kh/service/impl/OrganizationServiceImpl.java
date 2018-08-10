package com.sf.kh.service.impl;

import code.ponfee.commons.constrain.Constraint;
import code.ponfee.commons.log.LogAnnotation;
import code.ponfee.commons.model.PageHandler;
import code.ponfee.commons.model.ResultCode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sf.kh.exception.BusinessException;
import com.sf.kh.dao.IOrganizationDao;
import com.sf.kh.model.*;
import com.sf.kh.service.IDepartmentService;
import com.sf.kh.service.IOrganizationLevelService;
import com.sf.kh.service.IOrganizationService;
import com.sf.kh.service.IOrganizationTypeService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Auther: 01378178
 * @Date: 2018/6/15 15:12
 * @Description:
 */
@Service
public class OrganizationServiceImpl implements IOrganizationService {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    @Resource
    private IOrganizationDao organizationDao;

    @Resource
    private IOrganizationLevelService organizationLevelService;

    @Resource
    private IDepartmentService departmentService;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private IOrganizationTypeService organizationTypeService;

    @Override
    public List<Organization> getValidOrgsByOrgTypeId(Long orgTypeId) {

        if(orgTypeId == null){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "组织类型Id不能为空");
        }
        Map<String, Object> params = ImmutableMap.of("orgTypeId", orgTypeId, Constants.VALID, ValidStatusEnum.VALID.getCode());
        List<Organization> orgs = organizationDao.query4list(params);
        return orgs == null ? Lists.newArrayList() : orgs;
    }

    @LogAnnotation(type = LogAnnotation.LogType.QUERY)
    @Constraint(field = "orgTypeId", notNull = true)
    @Override
    public List<Organization> getValidTopOrgsByOrgTypeId(Long orgTypeId) {

        Map<String, Object> params = ImmutableMap.of("orgTypeId", orgTypeId, "parentOrgId", 0L, Constants.VALID, ValidStatusEnum.VALID.getCode());
        List<Organization> orgs = organizationDao.query4list(params);
        return orgs == null ? Lists.newArrayList() : orgs;
    }

    @LogAnnotation(type = LogAnnotation.LogType.QUERY)
    @Override
    public List<Organization> getValidTopOrgsByOrgTypeIds(List<Long> orgTypeIds){

        if(CollectionUtils.isEmpty(orgTypeIds)){
            return Lists.newArrayList();
        }

        Map<String, Object> params = ImmutableMap.of("orgTypeIdList", orgTypeIds, "parentOrgId", 0L,Constants.VALID, ValidStatusEnum.VALID.getCode());
        List<Organization> orgs = organizationDao.query4list(params);
        return orgs == null ? Lists.newArrayList() : orgs;
    }

    @Override
    public Organization getById(Long id) {
        return organizationDao.getById(id);
    }

    @Override
    public List<Organization> getOrgByIds(List<Long> ids) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("orgIdList", ids);
        params.put(Constants.VALID, ValidStatusEnum.VALID.getCode());
        return this.list(params);
    }

    @LogAnnotation(type = LogAnnotation.LogType.QUERY)
    @Override
    public List<Organization> getTopOrgsWithMaterialMark(){
        List<OrganizationType> orgTypes = organizationTypeService.getOrgTypeWithMaterialMark();
        if(CollectionUtils.isNotEmpty(orgTypes)){
            List<Long> ids = Lists.newArrayList();
            for(OrganizationType org : orgTypes){
                ids.add(org.getId());
            }
            //
            return this.getValidTopOrgsByOrgTypeIds(ids);
        }
        return Lists.newArrayList();
    }

    @LogAnnotation(type = LogAnnotation.LogType.QUERY)
    @Constraint(field = "orgId", notNull = true)
    @Override
    public List<Organization> getSubOrgs(Long orgId) {

        Map<String, Object> params = ImmutableMap.of("parentOrgId", orgId, Constants.VALID, ValidStatusEnum.VALID.getCode());
        List<Organization> orgs = organizationDao.query4list(params);

        return CollectionUtils.isEmpty(orgs)? Lists.newArrayList() : orgs;
    }

    @Override
    public int disableOrg(Organization org) {


        // 是否存在该组织
        Organization dbOrg = organizationDao.getById(org.getId());
        if(dbOrg == null){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "组织不存在");
        }

        Long orgId = org.getId();

        // 检查是否存在有效单位
        Department deptQuery = new Department();
        deptQuery.setOrgId(orgId);
        deptQuery.setValid(ValidStatusEnum.VALID.getCode());
        deptQuery.setHeadQuarter(Department.HeadQuarterEnum.NO.getCode());
        Map<String, Object> params = ConverterUtil.convertToMap(deptQuery);
        params.put("pageSize", 1);
        params.put("pageNum", 1);
        PageHandler.NORMAL.handle(params);
        List<Department> validDepts = departmentService.list(params);
        if(CollectionUtils.isNotEmpty(validDepts)){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "存在使用中的单位["+validDepts.get(0).getDeptName()+"], 请先删除相应单位");
        }

        // 获取总部
        Department hq = departmentService.getHeadQuarterForOrgId(orgId);


        // 检查是否存在有效非 总部 子单位
        Organization query = new Organization();
        query.setParentOrgId(orgId);
        query.setValid(ValidStatusEnum.VALID.getCode());

        params = ConverterUtil.convertToMap(query);
        params.put("pageSize", 1);
        params.put("pageNum", 1);

        PageHandler.NORMAL.handle(params);

        List<Organization> children = organizationDao.query4list(params);
        if(CollectionUtils.isNotEmpty(children)){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "存在使用中的子组织["+children.get(0).getOrgName()+"], 请先删除相应组织");
        }


        // 校验该组织下是否存在其他org, 如果不存在， 则删除组织层级
        params.clear();
        params.put("levelId", dbOrg.getLevelId());
        params.put("valid", ValidStatusEnum.VALID.getCode());

        List<Organization> sameLevelOrgs = organizationDao.query4list(params);
        List<Organization> otherOrgsInSameLevel = sameLevelOrgs.stream().filter(o -> !o.getId().equals(org.getId())).collect(Collectors.toList());


        Date d = new Date();
        User user = WebContextHolder.currentUser();

        Organization upd = new Organization();
        upd.setId(orgId);
        upd.setValid(ValidStatusEnum.INVALID.getCode());
        upd.setUpdateTm(d);
        upd.setUpdateBy(user==null?null:user.getId());

        if(CollectionUtils.isNotEmpty(otherOrgsInSameLevel)){

            return transactionTemplate.execute( t->{
                //disable dept that
                if(hq!=null && hq.getValid().equals(ValidStatusEnum.VALID.getCode())){
                    departmentService.disableDept(hq, false);
                }
                return organizationDao.updateOrganizationById(upd);
            });
        }else{
            //delete orgLevel and set invalid
            return transactionTemplate.execute( t->{
                //disable dept that
                if(hq!=null && hq.getValid().equals(ValidStatusEnum.VALID.getCode())){
                    departmentService.disableDept(hq, false);
                }
                organizationLevelService.deleteOrgLevelById(dbOrg.getLevelId());
                return organizationDao.updateOrganizationById(upd);
            });
        }

    }

    @LogAnnotation(type = LogAnnotation.LogType.UPDATE)
    @Constraint(field = "id", notNull = true)
    @Override
    public int updateOrg(Organization org) {

        Organization self = organizationDao.getById(org.getId());

        if (null == self) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "数据不存在");
        }

        if(StringUtils.isNotBlank(org.getOrgName()) && !self.getOrgName().equals(org.getOrgName())){

            List<Organization> existOrgs = getOrgByNameAndTypeId(org.getOrgName(), self.getParentOrgId(), self.getOrgTypeId());

            boolean sameNameExists = false;
            if (CollectionUtils.isNotEmpty(existOrgs)) {
                for (Organization o : existOrgs) {
                    if (!o.getId().equals(self.getId())) {
                        sameNameExists = true;
                        break;
                    }
                }
            }
            if (sameNameExists) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "已存在同名组织");
            }
        }

        return organizationDao.updateOrganizationById(org);
    }



    public OrganizationLevel getLevelToLink(Long orgTypeId, Long parenOrgId, String levelName){

        User user = WebContextHolder.currentUser();
        Date date = new Date();

        if(parenOrgId==null || parenOrgId.equals(0L)){

            List<Organization> sibs = this.getValidTopOrgsByOrgTypeId(orgTypeId);
            if(CollectionUtils.isNotEmpty(sibs)){
                return organizationLevelService.getOrganizationLevelById(sibs.get(0).getLevelId());
            }else{
                // create new top org Level
                Map<String, Object> query = Maps.newHashMap();
                query.put("orgTypeId", orgTypeId);
                query.put("levelName", levelName);

                List<OrganizationLevel> exists = organizationLevelService.list(query);
                if(CollectionUtils.isNotEmpty(exists)){
                    return exists.get(0);
                }

                OrganizationLevel level = new OrganizationLevel();
                level.setLevelName(levelName);
                level.setParentLevelId(0L);
                level.setOrgTypeId(orgTypeId);
                level.setCreateBy(user==null?null : user.getId());
                level.setCreateTm(date);
                level.setUpdateBy(user==null?null : user.getId());
                level.setUpdateTm(date);
                level.setVersion(1);

                int i = organizationLevelService.addOrganizationLevel(level);
                if(i!=1){
                    throw new BusinessException(ResultCode.SERVER_ERROR.getCode(), "添加组织级别失败,请重试");
                }
                return level;
            }
        }else{
            Organization pOrg = this.getById(parenOrgId);
            OrganizationLevel orgLevel = organizationLevelService.getSubOrgLevel(pOrg.getLevelId());

            if(orgLevel!=null){
                return orgLevel;
            }

            // validate whether such orgLevelName exist.
            Map<String, Object> queryMap = Maps.newHashMap();
            queryMap.put("levelName", levelName);
            queryMap.put("orgTypeId", orgTypeId);

            List<OrganizationLevel> duplicates = organizationLevelService.list(queryMap);
            if(CollectionUtils.isNotEmpty(duplicates)){
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "同名组织级别已经存在");
            }

            OrganizationLevel pLevel = organizationLevelService.getOrganizationLevelById(pOrg.getLevelId());
            // create new Org Level with with ParentOrgLevel
            OrganizationLevel level = new OrganizationLevel();
            level.setLevelName(levelName);
            level.setParentLevelId(pLevel.getId());
            level.setOrgTypeId(orgTypeId);
            level.setCreateBy(user==null?null : user.getId());
            level.setCreateTm(date);
            level.setUpdateBy(user==null?null : user.getId());
            level.setUpdateTm(date);
            level.setVersion(1);

            int i= organizationLevelService.addOrganizationLevel(level);

            if(i!=1){
                throw new BusinessException(ResultCode.SERVER_ERROR.getCode(), "添加组织级别失败,请重试");
            }
            return level;
        }
    }


    @LogAnnotation(type = LogAnnotation.LogType.ADD)
    @Constraint(field = "orgName", notNull = true, maxLen = 50)
    @Constraint(field="orgTypeId", notNull = true)
    @Override
    public int addOrg(Organization org) {

        String orgName = org.getOrgName();
        Long parentOrgId = org.getParentOrgId();
        Long orgTypeId = org.getOrgTypeId();
        String levelName = org.getLevelName();

        validateOrgExistence(orgName, parentOrgId, orgTypeId);

        boolean hasParentOrgId = org.getParentOrgId()!=null && !org.getParentOrgId().equals(0L);
        Organization parentOrg = hasParentOrgId ?  organizationDao.getById(org.getParentOrgId()) : null;

        //  如果没有级别名称, 则默认代表有同级的 组织 存在.

        return transactionTemplate.execute(
                o -> {
                    Long uptParentId = hasParentOrgId ? parentOrgId : 0L;
                    String parentOrgPath = (parentOrg != null ? parentOrg.getOrgPath() : null);
                    OrganizationLevel orgLeve = getLevelToLink(orgTypeId, parentOrgId, levelName);

                    org.setLevelName(orgLeve.getLevelName());
                    org.setLevelId(orgLeve.getId());
                    org.setParentOrgId(uptParentId);
                    org.setValid(ValidStatusEnum.VALID.getCode());
                    org.setVersion(1 );

                    organizationDao.insertOrganization(org);

                    // addHeadQuarterFo
                    int i = departmentService.addHeadQuarterForOrg(org);
                    if(i!=1){
                        logger.warn("add org, add head quarter for org failed org={}",org);
                        throw new BusinessException(ResultCode.SERVER_ERROR.getCode(), "添加组织总部失败,请重试");
                    }

                    // update organization id
                    Organization uptOrg = new Organization();
                    uptOrg.setId(org.getId());
                    uptOrg.setTopOrgId( parentOrg == null ? org.getId() : parentOrg.getTopOrgId());
                    uptOrg.setOrgPath(Organization.buildOrgPath(parentOrgPath, String.valueOf(org.getId())));
                    return organizationDao.updateOrganizationById(uptOrg);

                });
    }

    /**
     *
     * @param parentOrgId
     * @param orgTypeId
     */
    public Organization getSiblingOrg(Long parentOrgId, Long orgTypeId){

        // same level, then get level from sibling org.
        Organization queryOrg = new Organization();
        queryOrg.setParentOrgId(parentOrgId);
        queryOrg.setOrgTypeId(orgTypeId);

        // don't exist sibling organization. which represent that it's a new org, but no orgLevelName
        List<Organization> siblingOrgs = organizationDao.query4list(ConverterUtil.convertToMap(queryOrg));
        if(CollectionUtils.isEmpty(siblingOrgs)){
            logger.warn("add new org for a new org level, but no level name, parentOrgId={}, orgTypeId={}", parentOrgId, orgTypeId);
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "新级别组织，需输入级别名称");
        }

        // 检查siblingOrg 的级别id和级别名称是否存在, 不存在则抛异常
        Organization siblingOrg = siblingOrgs.get(0);
        if(siblingOrg.getLevelId() == null || StringUtils.isBlank(siblingOrg.getLevelName())){
            logger.warn("add organization, no level name and sibling org has no level either. siblingOrgId={}", siblingOrg.getId());
            throw new BusinessException(ResultCode.SERVER_ERROR.getCode(),"存在无级别的组织");
        }

        return siblingOrg;
    }

    /**
     * 检查同一级别 同名组织是否存在, 存在则抛异常
     * @param orgName
     * @return
     */
    public void validateOrgExistence(String orgName, Long parentOrgId, Long orgTypeId){

        Map<String, Object> params = ImmutableMap.of("orgName", orgName, "orgTypeId", orgTypeId,  "parentOrgId",  parentOrgId, "valid", ValidStatusEnum.VALID.getCode());
        List<Organization> orgs = organizationDao.query4list(params);

        if(CollectionUtils.isNotEmpty(orgs)) {
            logger.warn("add organization failed, org name exists at the same level. orgName={}, orgTypeId={}", orgName, orgTypeId);
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "同名节点已经存在");
        }
    }


    @LogAnnotation(type = LogAnnotation.LogType.QUERY)
    @Override
    public List<Organization> getOrgByNameAndTypeId(String orgName, Long parentOrgId, Long orgTypeId) {

        if (StringUtils.isBlank(orgName)) {
            return Lists.newArrayList();
        }

        Map<String, Object> params = Maps.newHashMap();
        params.put("orgName", orgName);
        params.put("orgTypeId", orgTypeId);
        params.put("parentOrgId", parentOrgId);
        params.put("valid", ValidStatusEnum.VALID.getCode());

        return organizationDao.query4list(params);
    }


    @Override
    public List<Organization> list(Map<String, ?> params) {
        return organizationDao.query4list(params);
    }

    /**
     *
     * @param orgName
     * @return
     */
    @Override
    public List<Organization> getAscendingHierarchicalOrgByNameForMaterial(String orgName) {

        if(StringUtils.isBlank(orgName)){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(),"组织名称不能为空");
        }

        // 业务约定, 只有一个组织类型是相关的
        List<OrganizationType> types = organizationTypeService.getOrgTypeWithMaterialMark();
        if(CollectionUtils.isEmpty(types)){
            return Lists.newArrayList();
        }

        List<Organization> orgs = this.getOrgByNameAndTypeId(orgName, null,  types.get(0).getId());

        if(CollectionUtils.isNotEmpty(orgs)){

            // to get All orgId
            List<Long> orgIds = Lists.newArrayList();
            for(Organization org : orgs){
                if(!org.isTopOrg()){
                    orgIds.addAll(org.splitOrgPath());
                }
            }

            Map<Long, Organization> orgMap = Maps.newHashMap();
            if (CollectionUtils.isNotEmpty(orgIds)) {

                Map<String, Object> params = Maps.newHashMap();
                params.put("orgIdList", orgIds);
                params.put("valid", ValidStatusEnum.VALID.getCode());

                List<Organization> torgs = organizationDao.query4list(params);
                orgMap = buildHierarchicalOrgs(torgs);
            }

            for(Organization org : orgs){
                org.setParentOrganization(orgMap.get(org.getParentOrgId()));
            }
        }

        return orgs;
    }

    public Map<Long, Organization> buildHierarchicalOrgs(List<Organization> relevantOrgs){
        Map<Long, Organization> orgMap = Maps.newHashMap();
        //所有的组织
        if(CollectionUtils.isNotEmpty(relevantOrgs)){
            orgMap = relevantOrgs.stream().collect(Collectors.toMap(Organization::getId, Function.identity()));

            // 构建好层级
            for(Organization org : relevantOrgs){
                if(org.getParentOrgId()!=null && !org.getParentOrgId().equals(0L)){
                    orgMap.get(org.getId()).setParentOrganization(orgMap.get(org.getParentOrgId()));
                }
            }
        }
        return orgMap;
    }

    @Override
    public Organization getAscendingHierarchicalOrgById(Long id) {
        if(id == null){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "id不能为空");
        }

        Organization org = organizationDao.getById(id);
        if(org!=null && !org.isTopOrg()){
            List<Long> orgIds = org.splitOrgPath();
            if(CollectionUtils.isNotEmpty(orgIds)){

                Map<String, Object> params = Maps.newHashMap();

                params.put("orgIdList", orgIds);
                params.put(Constants.VALID, ValidStatusEnum.VALID.getCode());
                List<Organization> relevantOrgs = organizationDao.query4list(params);

                Map<Long, Organization> orgMap = buildHierarchicalOrgs(relevantOrgs);
                org.setParentOrganization(orgMap.get(org.getParentOrgId()));
            }
        }

        return org;
    }

    @Override
    public List<String> listOrgNameByOrgLevelName(long orgTypeId, String levelName) {
        return organizationDao.listOrgNameByOrgLevelName(orgTypeId, levelName);
    }




}
