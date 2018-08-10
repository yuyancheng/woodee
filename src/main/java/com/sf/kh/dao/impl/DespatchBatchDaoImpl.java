package com.sf.kh.dao.impl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageHandler;

import com.sf.kh.dao.IDespatchBatchDao;
import com.sf.kh.dao.mapper.DespatchBatchMapper;
import com.sf.kh.model.DespatchBatch;

/***
 * 
 * @author 866316
 *
 */
@Repository
public class DespatchBatchDaoImpl implements IDespatchBatchDao {

    private @Resource DespatchBatchMapper despatchBatchMapper;

	@Override
	public DespatchBatch getByBatchId(String batchId) {
		return despatchBatchMapper.getByBatchId(batchId);
	}

	@Override
	public Page<DespatchBatch> query4page(Map<String, ?> params) {
		PageHandler.NORMAL.handle(params);
        return new Page<>(despatchBatchMapper.query4list(params));
	}

	@Override
	public DespatchBatch getTodayMaxBatchCode(String dateStr) {
		return despatchBatchMapper.getTodayMaxBatchCode(dateStr);
	}
	
	@Override
    public int  insertDespatchBatch(DespatchBatch despatchBatch){
		return despatchBatchMapper.insert(despatchBatch);
	}
	
	@Override
	public  int updateWaybill(List<DespatchBatch> list){
		return despatchBatchMapper.updateWaybill(list);
	}

	@Override
	public DespatchBatch insertBatchId(DespatchBatch despatchBatch) {
		Long result = despatchBatchMapper.insertBatchId(despatchBatch);
		if(null == result){
			return null;
		}else{
			return despatchBatch;
		}
	}
	
	@Override
	public int updateByBatchId(DespatchBatch despatchBatch) {
		return despatchBatchMapper.updateByBatchId(despatchBatch);
	}

	@Override
	public int updatePrintTime(String batchId, String printTm, Long userId) {
		return despatchBatchMapper.updatePrintTm(printTm, batchId,userId);
	}

}
