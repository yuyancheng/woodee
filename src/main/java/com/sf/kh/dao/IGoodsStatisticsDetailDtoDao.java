package com.sf.kh.dao;

import java.util.List;
import java.util.Map;

import code.ponfee.commons.model.Page;

import com.sf.kh.dto.GoodsStatisticsDetailDto;

/***
 * 物资统计-明细
 * @author 866316
 *
 */
public interface IGoodsStatisticsDetailDtoDao {
	
	Page<GoodsStatisticsDetailDto> query4page(Map<String, ?> params);
	
	List<GoodsStatisticsDetailDto> exportAllList(Map<String, ?> params);
	
}
