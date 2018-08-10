package com.sf.kh.dao.mapper;

import java.util.List;

import com.sf.kh.model.Role;
import com.sf.kh.model.UserRole;

/**
 * The mybatis mapper for table t_user_role
 * 
 * @author 01367825
 */
public interface UserRoleMapper {

    int insert(List<UserRole> list);

    int delByUserId(long userId);

    int delByRoleId(long roleId);

    List<Role> queryByUserId(long userId);

    List<String> queryUserPermits(long userId);
}