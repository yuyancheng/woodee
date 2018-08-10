package com.sf.kh.service;

import com.sf.kh.model.Organization;

import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/6/15 14:59
 * @Description:
 */
public interface IOrganizationService {


    /**
     * 根据 组织类型 id 获取组织
     * @param orgTypeId
     * @return
     */
    List<Organization> getValidOrgsByOrgTypeId(Long orgTypeId);

    /**
     * 根据 组织类型获取 一级组织
     * 一级组织定义：父组织id 等于自己 id
     * @param orgTypeId
     * @return
     */
    List<Organization> getValidTopOrgsByOrgTypeId(Long orgTypeId);

    /**
     * 获取orgId 的所有子级组织
     * @param parentOrgId
     * @return
     */
    List<Organization> getSubOrgs(Long parentOrgId);

    /**
     * 根据名称查询组织
     * @param orgName
     * @Param TypeId
     * @return
     */
    List<Organization> getOrgByNameAndTypeId(String orgName, Long parentOrgId, Long orgTypeId);

    int disableOrg(Organization org);

    int updateOrg(Organization org);

    int addOrg(Organization org);

    List<Organization> list(Map<String, ?> params);

    /**
     * 根据组名称查询组织, 并填充父级组织对象
     * @param orgName
     * @return
     */
    List<Organization> getAscendingHierarchicalOrgByNameForMaterial(String orgName);

    /**
     * 构建层级结构的组织 [内聚, 应收起来]
     * @param relevantOrgs
     * @return
     */
    Map<Long, Organization> buildHierarchicalOrgs(List<Organization> relevantOrgs);

    /**
     * 根据id查询组织, 带层级结构
     * @param id
     * @return
     */
    Organization getAscendingHierarchicalOrgById(Long id);

    /**
     * 获取 有效物料标志的 顶级组织
     * @return
     */
    List<Organization> getTopOrgsWithMaterialMark();

    /**
     * 根据组织类型id 获取有效的顶级组织
     * @param orgTypeIds
     * @return
     */
    List<Organization> getValidTopOrgsByOrgTypeIds(List<Long> orgTypeIds);

    /**
     * 根据id获取组织
     * @param id
     * @return
     */
    Organization getById(Long id);

    /**
     * 批量根据id获取有效组织
     * @param ids
     * @return
     */
    List<Organization> getOrgByIds(List<Long> ids);

    List<String> listOrgNameByOrgLevelName(long orgTypeId, String levelName);
}
