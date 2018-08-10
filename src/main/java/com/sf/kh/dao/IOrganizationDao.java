package com.sf.kh.dao;

import com.sf.kh.model.Organization;

import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/6/14 16:40
 * @Description:
 */
public interface IOrganizationDao {

    Organization getById(Long id);

    List<Organization> query4list(Map<String, ?> params);

    int insertOrganization(Organization org);

    int updateOrganizationById(Organization org);

    List<String> listOrgNameByOrgLevelName(long orgTypeId, String levelName);
}
