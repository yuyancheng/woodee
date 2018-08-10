package com.sf.kh.dao.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;
import com.sf.kh.dao.IGoodsStatisticsDetailDtoDao;
import com.sf.kh.dao.mapper.GoodsStatisticsDetailDtoMapper;
import com.sf.kh.dto.GoodsStatisticsDetailDto;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageHandler;

/****
 * 物资统计-明细
 * @author 866316
 *
 */
@Repository
public class GoodsStatisticsDetailDtoDaoImpl implements IGoodsStatisticsDetailDtoDao {

    private @Resource GoodsStatisticsDetailDtoMapper goodsStatisticsDetailDtoMapper;
    

	@Override
	public  Page<GoodsStatisticsDetailDto> query4page(Map<String, ?> params) {
		PageHandler.NORMAL.handle(params);
        return new Page<>(goodsStatisticsDetailDtoMapper.query4list(params));
	}
	
	@Override
	public List<GoodsStatisticsDetailDto> exportAllList(Map<String, ?> params){
		return goodsStatisticsDetailDtoMapper.exportAllList(params);
	}
}
