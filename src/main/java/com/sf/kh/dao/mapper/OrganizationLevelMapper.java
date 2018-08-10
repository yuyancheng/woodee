package com.sf.kh.dao.mapper;


import com.sf.kh.model.OrganizationLevel;

import java.util.List;
import java.util.Map;

public interface OrganizationLevelMapper {

    int deleteByPrimaryKey(Long id);

    int insert(OrganizationLevel record);

    int insertSelective(OrganizationLevel record);

    OrganizationLevel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrganizationLevel record);

    int updateByPrimaryKey(OrganizationLevel record);

    List<OrganizationLevel> query4list(Map<String, Object> params);
}