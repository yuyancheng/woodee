package com.sf.kh.dao.impl;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.sf.kh.dao.IGoodsDeptDao;
import com.sf.kh.dao.mapper.GoodsDeptMapper;
import com.sf.kh.model.GoodsDept;

/****
 * 基础维表dao
 * @author 866316
 *
 */
@Repository
public class GoodsDeptDaoImpl implements IGoodsDeptDao {

    private @Resource GoodsDeptMapper goodsDeptMapper;


	@Override
	public GoodsDept insert(GoodsDept goodsDept) {
		goodsDeptMapper.insert(goodsDept);
		return goodsDept;
	}

	@Override
	public List<GoodsDept> getGoodsDeptId(GoodsDept goodsDept){
		return goodsDeptMapper.select(goodsDept);
	}
	
	@Override
	public GoodsDept getGoodsDeptById(Long goodsDeptId){
		return goodsDeptMapper.selectById(goodsDeptId);
	}
}
