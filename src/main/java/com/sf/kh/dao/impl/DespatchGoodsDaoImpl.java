package com.sf.kh.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.sf.kh.dao.IDespatchGoodsDao;
import com.sf.kh.dao.mapper.DespatchGoodsMapper;
import com.sf.kh.model.DespatchGoods;

/***
 * 
 * @author 866316
 *
 */
@Repository
public class DespatchGoodsDaoImpl implements IDespatchGoodsDao {

    private @Resource DespatchGoodsMapper despatchGoodsMapper;

	@Override
	public List<DespatchGoods> getByBatchId(String batchId) {
		return despatchGoodsMapper.getByBatchId(batchId);
	}

	@Override
	public Boolean saveList(List<DespatchGoods> list) {
		Integer result = despatchGoodsMapper.saveList(list);
		if(result > 0){
			return true;
		}else{
			return false;
		}
	}


}
