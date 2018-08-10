package com.sf.kh.dao.mapper;

import java.util.List;
import java.util.Map;
import com.sf.kh.dto.GoodsStatisticsDetailDto;
/***
 * 物资统计-方向
 * @author 866316
 *
 */
public interface GoodsStatisticsDetailDtoMapper {
	/***
	 * 
	 * @return
	 */
	List<GoodsStatisticsDetailDto> query4list(Map<String, ?> params);
	
	/***
	 * 
	 * @return
	 */
	List<GoodsStatisticsDetailDto> exportAllList(Map<String, ?> params);
}