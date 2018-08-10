package com.sf.kh.dao;

import com.sf.kh.model.OrganizationLevel;

import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/6/20 18:06
 * @Description:
 */
public interface IOrganizationLevelDao {

    OrganizationLevel getById(Long id);

    List<OrganizationLevel> query4list(Map<String, Object> params);

    int insertOrganizationLevel(OrganizationLevel level);

    int updateOrganizationLevelById(OrganizationLevel level);

    int deleteLevelById(Long id);
}
