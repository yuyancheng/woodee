package com.sf.kh.dao;


import java.util.List;
import java.util.Map;

import code.ponfee.commons.model.Page;

import com.sf.kh.model.DespatchBatch;

/**
 * 
 * @author 866316
 *
 */
public interface IDespatchBatchDao {
 
    DespatchBatch getByBatchId(String batchId);
    
    Page<DespatchBatch> query4page(Map<String, ?> params);
    
    DespatchBatch getTodayMaxBatchCode(String dateStr);
    
    int  insertDespatchBatch(DespatchBatch despatchBatch);
    
    int updateWaybill(List<DespatchBatch> list);
    
    DespatchBatch insertBatchId(DespatchBatch despatchBatch);
    
    int updateByBatchId(DespatchBatch despatchBatch);
    
    int updatePrintTime(String batchId,String printTm,Long userId);
    
}
