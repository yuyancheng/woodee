package com.sf.kh.dao;

import java.util.List;

import com.sf.kh.model.GoodsDept;

/**
 * 
 * @author 866316
 *
 */
public interface IGoodsDeptDao {
    
	public GoodsDept insert(GoodsDept goodsDept);
	/***
	 * 根据其他id，获取主键id
	 * @param goodsDept
	 * @return
	 */
	public List<GoodsDept> getGoodsDeptId(GoodsDept goodsDept);
	
	public GoodsDept getGoodsDeptById(Long goodsDeptId);
}
