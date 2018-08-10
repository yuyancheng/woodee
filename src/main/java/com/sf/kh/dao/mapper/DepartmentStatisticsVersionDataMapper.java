package com.sf.kh.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sf.kh.model.DepartmentStatisticsVersionData;
/***
 * 物资统计-方向
 * @author 866316
 *
 */
public interface DepartmentStatisticsVersionDataMapper {
	/***
	 * 
	 * @param deptNos
	 * @param type：1，我发的；2，我收的
	 * @return
	 */
	List<DepartmentStatisticsVersionData> selectByDeptNosAndType(@Param("deptNos")List<Long> deptNos,
			@Param("type") String type);
}