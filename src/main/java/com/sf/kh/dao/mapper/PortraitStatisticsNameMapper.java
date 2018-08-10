package com.sf.kh.dao.mapper;

import java.util.List;

import com.sf.kh.dto.PortraitStatisticsDto;
import com.sf.kh.model.PortraitStatisticsName;
/***
 * 物资纵向统计-类别/品名统计top10
 * @author 866316
 *
 */
public interface PortraitStatisticsNameMapper {
	
	/***
     * 根据提供的参数获取记录
     * @param record
     * @return
     */
    List<PortraitStatisticsName> getList(PortraitStatisticsDto  record);
    
}