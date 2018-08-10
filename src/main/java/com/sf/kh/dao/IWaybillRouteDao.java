package com.sf.kh.dao;

import java.util.List;

import com.sf.kh.model.Waybill;
import com.sf.kh.model.WaybillRoute;

public interface IWaybillRouteDao {
	Integer saveList(List<WaybillRoute> list);
	
	WaybillRoute getTop1ByWaybillNo(String waybillNo);
	
	Integer deleteByWaybillNo(List<String> waybillNos);

	List<WaybillRoute> getByWaybillNo(String waybillNO);
	
	List<String> getSignWaybillsByRecent15Days();
}
