package com.sf.kh.dao;


import java.util.List;
import com.sf.kh.model.DespatchGoods;

/**
 * 
 * @author 866316
 *
 */
public interface IDespatchGoodsDao {
    
    List<DespatchGoods> getByBatchId(String batchId);
    
    Boolean saveList(List<DespatchGoods> list);
}
