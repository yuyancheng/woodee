package com.sf.kh.dao.impl;

import com.sf.kh.dao.IDepartmentDao;
import com.sf.kh.dao.mapper.DepartmentMapper;
import com.sf.kh.model.Department;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/6/14 16:40
 * @Description:
 */

@Repository
public class DepartmentDaoImpl implements IDepartmentDao {

    @Resource
    private DepartmentMapper departmentMapper;

    @Override
    public Department getById(Long id) {
        return departmentMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Department> query4page(Map<String, ?> params) {
        return departmentMapper.query4list(params);
    }

    @Override
    public int insertDepartment(Department department) {
        return departmentMapper.insert(department);
    }

    @Override
    public int updateDepartment(Department dept) {
        return departmentMapper.updateByPrimaryKeySelective(dept);
    }

    @Override
    public List<Department> selectByOrgNameDeptName(Map<String, ?> params) {
        return departmentMapper.selectByOrgNameDeptName(params);
    }

    @Override
    public int batchUpdateDept(Map<String, ?> params) {
        List<Long> ids = (List<Long>) params.get("deptIds");
        if(CollectionUtils.isEmpty(ids)){
            return 0;
        }
        return departmentMapper.updateByIdsBatch(params);
    }

}
