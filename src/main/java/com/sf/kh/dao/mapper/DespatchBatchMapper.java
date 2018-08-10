package com.sf.kh.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.sf.kh.model.DespatchBatch;

/***
 * 
 * @author 866316
 *
 */
public interface DespatchBatchMapper {
    
    DespatchBatch getByBatchId(String batchId);
    
    List<DespatchBatch> query4list(Map<String, ?> params);
    
    DespatchBatch getTodayMaxBatchCode(String dateStr);
    
    int insert(DespatchBatch despatchBatch);
    
    /**
     * 更新批次号中的运单记录
     * @param list
     * @return
     */
    int updateWaybill(List<DespatchBatch> list);
    
    
    Long insertBatchId(DespatchBatch despatchBatch);
    
    int updateByBatchId(DespatchBatch despatchBatch);
    
    int updatePrintTm(@Param("printTm") String printTm,
    		@Param("batchId") String batchId,@Param("userId") Long userId);
}
