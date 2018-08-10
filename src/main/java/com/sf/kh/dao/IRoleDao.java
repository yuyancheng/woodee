package com.sf.kh.dao;

import java.util.Map;

import com.sf.kh.model.Role;

import code.ponfee.commons.model.Page;

/**
 * The database operator interface for role table
 *
 * @author Ponfee
 */
public interface IRoleDao {

    int add(Role role);

    int update(Role role);

    int delete(long roleId);

    Role getById(long id);

    Role getByRoleCode(String roleCode);

    Page<Role> query4page(Map<String, ?> params);
}
