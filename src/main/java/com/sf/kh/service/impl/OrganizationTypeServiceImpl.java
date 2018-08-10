package com.sf.kh.service.impl;

import code.ponfee.commons.constrain.Constraint;
import code.ponfee.commons.log.LogAnnotation;
import code.ponfee.commons.model.ResultCode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sf.kh.dao.IOrganizationTypeDao;
import com.sf.kh.exception.BusinessException;
import com.sf.kh.model.Organization;
import com.sf.kh.model.OrganizationType;
import com.sf.kh.model.ValidStatusEnum;
import com.sf.kh.service.IOrganizationService;
import com.sf.kh.service.IOrganizationTypeService;
import com.sf.kh.util.Constants;
import com.sf.kh.util.ConverterUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/6/14 17:09
 * @Description:
 */
@Service
public class OrganizationTypeServiceImpl implements IOrganizationTypeService {

    private static Logger logger = LoggerFactory.getLogger(OrganizationTypeServiceImpl.class);

    @Resource
    private IOrganizationTypeDao organizationTypeDao;

    @Resource
    private IOrganizationService organizationService;


    @LogAnnotation(type = LogAnnotation.LogType.ADD)
    @Constraint(field = "orgTypeName", notNull = true, maxLen = 50, msg="名称超过50个字符")
    @Override
    public int addOrgType(OrganizationType orgType) {

        List<OrganizationType> existTypes = getOrgTypeByName(orgType.getOrgTypeName());

        if (CollectionUtils.isNotEmpty(existTypes)) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "名称已存在");
        }

        return organizationTypeDao.insertOrganizationType(orgType);
    }


    @Override
    public List<OrganizationType> getAllValidOrgType() {
        Map<String, Object> params = Maps.newHashMap();
        params.put(Constants.VALID, ValidStatusEnum.VALID.getCode());
        return organizationTypeDao.query4list(params);
    }

    @Override
    public List<OrganizationType> getAllOrgType() {
        return organizationTypeDao.query4list(Maps.newHashMap());
    }

    @Override
    public OrganizationType getOrgTypeById(Long id) {
        return organizationTypeDao.getById(id);
    }


    @LogAnnotation(type = LogAnnotation.LogType.UPDATE)
    @Constraint(field = "id", notNull = true)
    @Constraint(field = "orgTypeName", notNull = true, maxLen = 50, msg="名称超过50个字符")
    @Override
    public int updateOrgType(OrganizationType orgType) {

        OrganizationType self = getOrgTypeById(orgType.getId());

        if (null == self) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "数据不存在");
        }

        // check whether the orgTypeName already existed.
        List<OrganizationType> existTypes = getOrgTypeByName(orgType.getOrgTypeName());

        boolean sameNameExists = false;
        if (CollectionUtils.isNotEmpty(existTypes)) {
            for (OrganizationType type : existTypes) {
                if (!type.getId().equals(self.getId())) {
                    sameNameExists = true;
                    break;
                }
            }
        }

        if (sameNameExists) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "已存在同名组织类型");
        }

        return organizationTypeDao.updateOrganizationTypeById(orgType);
    }

    @LogAnnotation(type = LogAnnotation.LogType.UPDATE)
    @Override
    public int disableOrgType(OrganizationType orgType) {

        Organization query = new Organization();
        query.setValid(ValidStatusEnum.VALID.getCode());
        query.setOrgTypeId(orgType.getId());

        Map<String, Object> params = ConverterUtil.convertToMap(query);
        params.put("pageNum", 1);
        params.put("pageSize", 1);

        List<Organization> org = organizationService.list(params);
        if(CollectionUtils.isNotEmpty(org)){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(),"存在使用中部门["+org.get(0).getOrgName()+"], 请先删除相应部门");
        }

        orgType.setValid(ValidStatusEnum.INVALID.getCode());
        return organizationTypeDao.updateOrganizationTypeById(orgType);
    }


    @LogAnnotation(type = LogAnnotation.LogType.QUERY)
    @Override
    public List<OrganizationType> getOrgTypeByName(String orgTypeName) {

        if (StringUtils.isBlank(orgTypeName)) {
            return Lists.newArrayList();
        }

        Map<String, Object> params = Maps.newHashMap();
        params.put("orgTypeName", orgTypeName);
        params.put(Constants.VALID, ValidStatusEnum.VALID.getCode());

        return organizationTypeDao.query4list(params);
    }

    @Override
    public List<OrganizationType> list(Map<String, ?> params) {
        return organizationTypeDao.query4list(params);
    }

    @Override
    public List<OrganizationType> getOrgTypeWithMaterialMark() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("materialMark", 1);
        params.put("valid", 1);
        return organizationTypeDao.query4list(params);
    }

    @Override
    public List<OrganizationType> getValidOrgTypedByIds(List<Long> ids) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("orgTypeIds", ids);
        params.put("valid", ValidStatusEnum.VALID.getCode());
        return list(params);
    }
}
