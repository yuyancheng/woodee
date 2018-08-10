package com.sf.kh.dao.mapper;


import com.sf.kh.model.Department;

import java.util.List;
import java.util.Map;

public interface DepartmentMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Department record);

    int insertSelective(Department record);

    Department selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Department record);

    int updateByPrimaryKey(Department record);

    List<Department> query4list(Map<String, ?> params);

    List<Department> selectByOrgNameDeptName(Map<String, ?> params);

    int updateByIdsBatch(Map<String, ?> params);
}