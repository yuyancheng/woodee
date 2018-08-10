package com.sf.kh.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.sf.kh.dao.IRolePermitDao;
import com.sf.kh.dao.mapper.RolePermitMapper;
import com.sf.kh.model.RolePermit;

/**
 * The IRolePermitDao implementation class
 *
 * @author Ponfee
 */
@Repository
public class RolePermitDaoImpl implements IRolePermitDao {

    private @Resource RolePermitMapper mapper;

    @Override
    public int add(List<RolePermit> list) {
        return mapper.insert(list);
    }

    @Override
    public int delByRoleId(long roleId) {
        return mapper.delByRoleId(roleId);
    }

    @Override
    public int delByPermitId(String permitId) {
        return mapper.delByPermitId(permitId);
    }

    @Override
    public List<String> queryPermitsByRoleId(long roleId) {
        return mapper.queryPermitsByRoleId(roleId);
    }

}
