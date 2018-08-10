package com.sf.kh.dao;

import java.util.List;

import com.sf.kh.model.TimeStatisticsVersionData;

/***
 * 物资统计-时间维度
 * @author 866316
 *
 */
public interface ITimeStatisticsVersionDataDao {
	/***
	 * 
	 * @param deptNos
	 * @param type，1，为我发的，2为我收的
	 * @return
	 */
	List<TimeStatisticsVersionData> getListByDeptNosAndType(List<Long> deptNos,String type);
}
