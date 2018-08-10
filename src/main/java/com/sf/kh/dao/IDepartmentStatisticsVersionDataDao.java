package com.sf.kh.dao;

import java.util.List;

import com.sf.kh.model.DepartmentStatisticsVersionData;

/***
 * 物资统计-单位方向
 * @author 866316
 *
 */
public interface IDepartmentStatisticsVersionDataDao {
	
	List<DepartmentStatisticsVersionData> selectByDeptNosAndType(
			List<Long> deptList, String type);
}
