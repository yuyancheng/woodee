package com.sf.kh.dao;

import java.util.List;

import com.sf.kh.model.RolePermit;

/**
 * The Role Permit Dao Interface
 *
 * @author Ponfee
 */
public interface IRolePermitDao {

    int add(List<RolePermit> list);

    int delByRoleId(long roleId);

    int delByPermitId(String permitId);

    List<String> queryPermitsByRoleId(long roleId);
}
