package com.sf.kh.service;

import com.sf.kh.model.Department;
import com.sf.kh.model.Organization;

import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/6/19 16:19
 * @Description:
 */
public interface IDepartmentService {

    /**
     * 根据组织id 获取单位
     * @return
     */
    List<Department> getValidDeptsByOrgId(Map<String, Object> params);


    /**
     * 根据组织类型获id 取有效单位
     * @return
     */
    List<Department> getValidDeptsByOrgTypeId(Long orgTypeId, Integer pageNum, Integer pageSize);

    /**
     * 根据单位id获取子单位 一层
     * @return
     */
    List<Department> getSubDepts(Long deptId);

    /**
     * 获取所有的单位 多层
     * @param deptId
     * @return
     */
    List<Department> getAllChildDepts(Long deptId);


    /**
     * 禁用单位
     * @param dept
     * id
     * @return
     */
    int disableDept(Department dept, boolean checkHQ);

    int updateDept(Department dept);

    int addDept(Department dept);

    List<Department> list(Map<String, ?> params);

    /**
     * 添加组织的总部
     * @param org
     * @return
     */
    int addHeadQuarterForOrg(Organization org);

    /**
     * 根据组织类型id获取单位(嵌套组织层级信息)
     * @param params
     * @return
     */
    List<Department> getDeptsWithHierarchicalOrgByOrgTypeId(Map<String, Object> params);

    /**
     * 根据组织id获取单位(嵌套组织层级信息)
     * @param params
     * @return
     */
    List<Department> getDeptsWithHierarchicalOrgByOrgId(Map<String, Object> params);

    /**
     * 根据单位id获取 单位信息(嵌套组织层级信息)
     * @param id
     * @return
     */
    Department getDeptWithHierarchicalOrgById(Long id);

    /**
     *  查询组织的总部
     * @param orgId
     * @return
     */
    Department getHeadQuarterForOrgId(Long orgId);

    /**
     * 根据组织名称， 单位名称获取单位.
     * @param orgName
     * @param deptName
     * @return
     */
    List<Department> getDepartmentByOrgNameAndDeptName(Long orgTypeId, String orgName, String deptName);


    /**
     * 批量禁用单位
     * @param dept
     * @return
     */
    int batchDisableDept(Department dept);

    /**
     * 添加单位
     * @param depts
     * @return
     */
    int addDepts(List<Department> depts);


    /**
     * 导入单位的 添加或者更新
     * 更新只更新 总部 信息.
     * @param addDepts
     * @param uptDepts
     * @return
     */
    int addOrUpdateDeptForImport(List<Department> addDepts, List<Department> uptDepts);
    /**
     * 根据id获取单位信息
     * @param id
     * @return
     */
    Department getById(Long id);

    /**
     * 根据月结卡号查询部门
     * @param custNoList
     * @return
     */
    List<Department> getDeptsByCustNoList(List<String> custNoList);


    /**
     * 查询部门
     * @param params
     *
     *  orgTypeId
     *  DeptName
     *  pageNum
     *  pageSize
     *  deptId
     *  deptNameFuzzy
     *
     * @return
     */
    List<Department> getDeptByOrgTypeIdDeptNameFuzzy(Map<String, Object> params);


    /**
     * 查询组织下的所有部门
     * @param orgId
     * @return
     */
    List<Department> getAllDeptsByOrgId(Long orgId);


    /**
     * 查询单位id
     *  传单位则直接返回单位id,
     *  传orgId, 则返回org下的所有单位id
     *  传orgTypeId, 则返回orgType下的所有单位
     * @param orgTypeId
     * @param orgId
     * @param deptId
     * @return
     */
    List<Long> getDeptIdsAccordingly(Long orgTypeId, Long orgId, Long deptId);

}
