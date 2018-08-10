package com.sf.kh.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.sf.kh.dao.IWaybillDao;
import com.sf.kh.dao.mapper.WaybillMapper;
import com.sf.kh.model.Waybill;

@Repository
public class WaybillDaoImpl implements IWaybillDao {

	private @Resource WaybillMapper wayBillMapper;
	@Override
	public Integer saveList(List<Waybill> list) {
		return wayBillMapper.saveList(list);
	}
	@Override
	public Waybill selectByWaybillNo(String waybillNo) {
		return wayBillMapper.selectByWaybillNo(waybillNo);
	}
	
	@Override
	public Integer updateByIds(List<Waybill> list){
		return wayBillMapper.updateByIds(list);
	}

	@Override
	public Integer saveOrUpdateList(List<Waybill> list){
		return wayBillMapper.saveOrUpdateList(list);
	}
}
