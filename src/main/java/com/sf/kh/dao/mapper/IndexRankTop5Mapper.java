package com.sf.kh.dao.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.sf.kh.model.IndexRankTop5;

public interface IndexRankTop5Mapper {
	
	List<IndexRankTop5> getTop5Result(@Param("type")String type,
			@Param("version")String version);
}