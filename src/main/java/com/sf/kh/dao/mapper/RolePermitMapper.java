package com.sf.kh.dao.mapper;

import java.util.List;

import com.sf.kh.model.RolePermit;

/**
 * The mybatis mapper for table t_role_permit
 * 
 * @author 01367825
 */
public interface RolePermitMapper {

    int insert(List<RolePermit> list);

    int delByRoleId(long roleId);

    int delByPermitId(String permitId);

    List<String> queryPermitsByRoleId(long roleId);

}