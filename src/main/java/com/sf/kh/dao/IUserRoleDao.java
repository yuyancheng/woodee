package com.sf.kh.dao;

import java.util.List;

import com.sf.kh.model.Role;
import com.sf.kh.model.UserRole;

/**
 * The database operator interface for t_user_role table
 *
 * @author Ponfee
 */
public interface IUserRoleDao {

    int add(List<UserRole> list);

    int delByUserId(long userId);

    int delByRoleId(long roleId);

    List<Role> queryByUserId(long userId);

    List<String> queryUserPermits(long userId);
}
