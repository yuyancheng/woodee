package com.sf.kh.dao.mapper;


import com.sf.kh.model.Organization;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface OrganizationMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Organization record);

    int insertSelective(Organization record);

    Organization selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Organization record);

    int updateByPrimaryKey(Organization record);

    List<Organization> query4list(Map<String, ?> params);

    List<String> listOrgNameByOrgLevelName(@Param("orgTypeId") long orgTypeId, 
                                           @Param("levelName") String levelName);
}