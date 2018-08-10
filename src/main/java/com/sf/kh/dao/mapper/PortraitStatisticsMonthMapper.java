package com.sf.kh.dao.mapper;

import java.util.List;

import com.sf.kh.dto.PortraitStatisticsDto;
import com.sf.kh.model.PortraitStatisticsMonth;
/***
 * 物资纵向统计-月份数据
 * @author 866316
 *
 */
public interface PortraitStatisticsMonthMapper {
	/***
     * 根据提供的参数获取记录
     * @param record
     * @return
     */
    List<PortraitStatisticsMonth> getList(PortraitStatisticsDto  record);
}