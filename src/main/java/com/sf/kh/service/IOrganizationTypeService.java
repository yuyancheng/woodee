package com.sf.kh.service;

import com.sf.kh.model.OrganizationType;

import java.util.List;
import java.util.Map;


/**
 * @Auther: 01378178
 * @Date: 2018/6/14 16:57
 * @Description:
 */
public interface IOrganizationTypeService {

    int addOrgType(OrganizationType orgType);

    List<OrganizationType> getAllValidOrgType();

    List<OrganizationType> getAllOrgType();

    OrganizationType getOrgTypeById(Long id);

    int updateOrgType(OrganizationType orgType);

    int disableOrgType(OrganizationType orgType);

    List<OrganizationType> getOrgTypeByName(String orgTypeName);

    List<OrganizationType> list(Map<String, ?> params);

    List<OrganizationType> getOrgTypeWithMaterialMark();

    List<OrganizationType> getValidOrgTypedByIds(List<Long> ids);
}
