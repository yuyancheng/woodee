package com.sf.kh.dao;

import java.util.List;

import com.sf.kh.model.CategoryStatisticsVersionData;

/***
 * 物资统计-物资类别
 * @author 866316
 *
 */
public interface ICategoryStatisticsVersionDataDao {
	
	List<CategoryStatisticsVersionData> getListByDeptNosAndType(
			List<Long> deptNos, String type);
}
