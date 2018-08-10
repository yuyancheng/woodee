package com.sf.kh.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sf.kh.model.OverviewVersionData;

public interface OverviewVersionDataMapper {
    
    List<OverviewVersionData> selectByCompanyIdsAndTypeAndVersion(@Param("companyIds")List<String> companyIds,
    		@Param("type")String type,@Param("version")Integer version);
}