package com.sf.kh.dao.mapper;

import com.sf.kh.dto.UserReceiveDto;

/**
 * The user mapper of mybatis
 *
 */
public interface UserReceiveDtoMapper {

    
    UserReceiveDto getUserInfoByDeptId(Long deptId);

}
