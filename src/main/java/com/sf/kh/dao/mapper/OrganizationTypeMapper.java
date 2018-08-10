package com.sf.kh.dao.mapper;


import com.sf.kh.model.OrganizationType;

import java.util.List;
import java.util.Map;

public interface OrganizationTypeMapper {

    int deleteByPrimaryKey(Long id);

    int insert(OrganizationType record);

    int insertSelective(OrganizationType record);

    OrganizationType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrganizationType record);

    int updateByPrimaryKey(OrganizationType record);

    List<OrganizationType> query4list(Map<String,  ?> params);
}