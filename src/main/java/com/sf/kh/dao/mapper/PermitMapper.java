package com.sf.kh.dao.mapper;

import java.util.List;

import com.sf.kh.model.Permit;

/**
 * The mybatis mapper for table t_permit
 * 
 * @author 01367825
 */
public interface PermitMapper {

    int insert(Permit permit);

    int update(Permit permit);

    int deleteByPermitIds(List<String> ids);

    Permit getByPermitId(String id);

    List<Permit> listAll();

}