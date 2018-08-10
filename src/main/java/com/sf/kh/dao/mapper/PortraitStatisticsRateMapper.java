package com.sf.kh.dao.mapper;

import java.util.List;

import com.sf.kh.dto.PortraitStatisticsDto;
import com.sf.kh.model.PortraitStatisticsRate;
/***
 * 物资纵向统计-发运类别/物资占比
 * @author 866316
 *
 */
public interface PortraitStatisticsRateMapper {
    
	/***
     * 根据提供的参数获取记录
     * @param record
     * @return
     */
    List<PortraitStatisticsRate> getList(PortraitStatisticsDto  record);
}