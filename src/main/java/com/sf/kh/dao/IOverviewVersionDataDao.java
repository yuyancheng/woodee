package com.sf.kh.dao;

import java.util.List;

import com.sf.kh.model.OverviewVersionData;

/***
 * 获取概览统计
 * @author 866316
 *
 */
public interface IOverviewVersionDataDao {
	
	List<OverviewVersionData> getListByCompanyIdsAndType(List<String> companyIds,String type);
	
}
