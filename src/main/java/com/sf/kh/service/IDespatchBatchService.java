package com.sf.kh.service;

import java.util.List;
import java.util.Map;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.Result;

import com.sf.kh.model.DespatchBatch;
import com.sf.kh.model.DespatchGoods;
/***
 * 发运单
 * @author 866316
 *
 */
public interface IDespatchBatchService {
	
	Result<Boolean> validBatchNumber(String batchId);
	
	Result<Page<DespatchBatch>> query4page(Map<String, ?> params);
	
	Result<DespatchBatch> print(String batchId,Long deptId);
	
	long getTodayMaxBatchCode();
	
	Result<String> insertDespatchBatch(DespatchBatch despatchBatch);
	
	DespatchBatch insertBatchId(DespatchBatch despatchBatch);
	
	Result<String> updateByBatchId(DespatchBatch despatchBatch);
	
	Result<List<DespatchGoods>> getGoodsList(String batchId,Long userId);

	Result<Boolean> updatePrintTime(String batchId, Long userId);


	Result<List<DespatchGoods>> getGoodsListWithBase(String batchIds);
}
