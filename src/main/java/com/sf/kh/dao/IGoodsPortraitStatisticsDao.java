package com.sf.kh.dao;

import java.util.List;
import com.sf.kh.dto.PortraitStatisticsDto;
import com.sf.kh.model.PortraitStatisticsDepartment;
import com.sf.kh.model.PortraitStatisticsMonth;
import com.sf.kh.model.PortraitStatisticsName;
import com.sf.kh.model.PortraitStatisticsRate;

/***
 * 物资纵向统计
 * @author 866316
 *
 */
public interface IGoodsPortraitStatisticsDao {
	/**
	 * 物资纵向统计-方向单位
	 * @param record
	 * @return
	 */
	List<PortraitStatisticsDepartment> getDeptList(PortraitStatisticsDto record);
	/***
	 * 物资纵向统计-类别/物资占比
	 * @param record
	 * @return
	 */
	List<PortraitStatisticsRate> getRateList(PortraitStatisticsDto record);
	/***
	 * 物资纵向统计-类别/物资发运量
	 * @param record
	 * @return
	 */
	List<PortraitStatisticsName> getNameList(PortraitStatisticsDto record);
	/***
	 * 物资纵向统计-时间发运量
	 * @param record
	 * @return
	 */
	List<PortraitStatisticsMonth> getMonthList(PortraitStatisticsDto record);
	
}
