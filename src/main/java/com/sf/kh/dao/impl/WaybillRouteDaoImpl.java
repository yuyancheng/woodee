package com.sf.kh.dao.impl;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.sf.kh.dao.IWaybillRouteDao;
import com.sf.kh.dao.mapper.WaybillRouteMapper;
import com.sf.kh.model.WaybillRoute;
import com.sf.kh.util.DateUtil;

@Repository
public class WaybillRouteDaoImpl implements IWaybillRouteDao {

	private @Resource WaybillRouteMapper wayBillRouteMapper;
	@Override
	public Integer saveList(List<WaybillRoute> list) {
		return wayBillRouteMapper.saveList(list);
	}
	@Override
	public WaybillRoute getTop1ByWaybillNo(String waybillNo) {
		return wayBillRouteMapper.getTop1ByWaybillNo(waybillNo);
	}
	
	@Override
	public Integer deleteByWaybillNo(List<String> waybillNos){
		return wayBillRouteMapper.deleteByWaybillNo(waybillNos);
	}


	@Override
	public List<WaybillRoute> getByWaybillNo(String waybillNo) {
		return wayBillRouteMapper.getByWaybillNo(waybillNo);
	}
	@Override
	public List<String> getSignWaybillsByRecent15Days() {
		Calendar calStart = Calendar.getInstance();
		calStart.add(Calendar.DATE, -15);
		String startDate = DateUtil.formatDate(calStart.getTime());
		Calendar calEnd = Calendar.getInstance();
		String endDate = DateUtil.formatDate(calEnd.getTime());
		return  wayBillRouteMapper.getSignWaybillsByRecent15Days(startDate,endDate);
	}

}
