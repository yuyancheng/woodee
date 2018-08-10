package com.sf.kh.service;

import com.sf.kh.model.OrganizationLevel;

import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/6/20 18:02
 * @Description:
 */
public interface IOrganizationLevelService {

    int addOrganizationLevel(OrganizationLevel level);

    OrganizationLevel getOrganizationLevelById(Long id);

    List<OrganizationLevel> getOrgLevelByOrgTypeId(Long orgTypeId);

    OrganizationLevel getHierarchicalOrgLevel(Long orgTypeId);

    OrganizationLevel getSubOrgLevel(Long levelId);

    List<OrganizationLevel> list(Map<String, Object> params);

    OrganizationLevel buildDescendingOrgLevel(List<OrganizationLevel> levels);

    int deleteOrgLevelById(Long id);

    /**
     * 根据组织类型获取组织层级名称, 从顶到底排序
     *
     * @param orgTypeId
     * @return
     */
    List<String> getDescendingOrgLevelName(Long orgTypeId);
}
