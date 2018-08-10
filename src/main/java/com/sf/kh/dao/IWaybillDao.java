package com.sf.kh.dao;

import java.util.List;

import com.sf.kh.model.Waybill;

public interface IWaybillDao {
	Integer saveList(List<Waybill> list);
	
	Waybill selectByWaybillNo(String waybillNo);
	
	Integer updateByIds(List<Waybill> list);
	
	Integer saveOrUpdateList(List<Waybill> list);
}
