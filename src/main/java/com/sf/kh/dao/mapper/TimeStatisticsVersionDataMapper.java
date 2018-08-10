package com.sf.kh.dao.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.sf.kh.model.TimeStatisticsVersionData;
/***
 * 物资统计-时间维度
 * @author 866316
 *
 */
public interface TimeStatisticsVersionDataMapper {
	
	List<TimeStatisticsVersionData> selectByDeptNosAndType(
			@Param("deptNos")List<Long> deptNos,
			@Param("type")String type);
}