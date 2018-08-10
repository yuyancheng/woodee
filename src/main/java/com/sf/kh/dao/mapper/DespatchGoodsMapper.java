package com.sf.kh.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sf.kh.model.DespatchGoods;


public interface DespatchGoodsMapper {
    
    List<DespatchGoods> getByBatchId(String batchId);
    
    Integer saveList(List<DespatchGoods> list);
}