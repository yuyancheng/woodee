package com.sf.kh.dao;

import com.sf.kh.model.OrganizationType;

import java.util.List;
import java.util.Map;

public interface IOrganizationTypeDao {

    OrganizationType getById(Long id);

    List<OrganizationType> query4list(Map<String, ?> params);

    int insertOrganizationType(OrganizationType orgType);

    int updateOrganizationTypeById(OrganizationType orgType);
}
