package com.sf.kh.dao;

import com.sf.kh.model.Department;

import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/6/14 16:40
 * @Description:
 */
public interface IDepartmentDao {

    Department getById(Long id);

    List<Department> query4page(Map<String, ?> params);

    int insertDepartment(Department department);

    int updateDepartment(Department dept);

    List<Department> selectByOrgNameDeptName(Map<String, ?> params);

    int batchUpdateDept(Map<String, ?> params);
}
