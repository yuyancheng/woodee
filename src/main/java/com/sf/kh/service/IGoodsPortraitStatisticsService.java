package com.sf.kh.service;

import java.util.List;
import java.util.Map;
import code.ponfee.commons.model.Result;
import com.sf.kh.dto.DepartmentStatisticsDto;
import com.sf.kh.dto.PortraitStatisticsDto;
import com.sf.kh.model.PortraitStatisticsMonth;
import com.sf.kh.model.PortraitStatisticsName;

public interface IGoodsPortraitStatisticsService{

	Result<Map<String,List<DepartmentStatisticsDto>>> getDepartmentStatisticsData(PortraitStatisticsDto psd);
	
	Result<List<PortraitStatisticsName>> getPortraitStatisticsNameData(PortraitStatisticsDto psd);
	
	Result<List<PortraitStatisticsMonth>> getPortraitStatisticsMonthData(PortraitStatisticsDto psd);
	
}
