package com.sf.kh.service;

import java.io.File;
import java.util.List;
import java.util.Map;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.Result;
import com.sf.kh.dto.CategoryStatisticsDto;
import com.sf.kh.dto.DepartmentStatisticsDto;
import com.sf.kh.dto.GoodsStatisticsDetailDto;
import com.sf.kh.dto.OverviewVersionDataDto;
import com.sf.kh.dto.TimeStatisticsDto;
import com.sf.kh.model.Department;

public interface IGoodsStatisticsService{

	Result<OverviewVersionDataDto> getOverviewVersionData(
			String custNo,Department myDept,List<Department> deptList);
	
	Result<Map<String,List<DepartmentStatisticsDto>>> getDepartmentStatisticsData(List<Department> deptList);
	
	
	Result<List<CategoryStatisticsDto>> getCategoryStatisticsData(List<Department> deptList);
	
	Result<List<TimeStatisticsDto>> getTimeStatisticsVersionData(List<Department> deptList);
	
	Result<Page<GoodsStatisticsDetailDto>> query4pageBySend(Map<String, Object> params);
	
	Result<Page<GoodsStatisticsDetailDto>> query4pageByReceive(Map<String, Object> params);
	
	File exportsGoodsList(String fileFolderPath,String type,Map<String, Object> params);
}
