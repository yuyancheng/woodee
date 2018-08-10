package com.sf.kh.service;

import java.util.List;
import java.util.Map;

import com.sf.kh.model.IndexRankTop5;

import code.ponfee.commons.model.Result;
/***
 * 首页top5
 * @author 866316
 *
 */
public interface IIndexRankTop5Service{
	
	Result<Map<String,List<IndexRankTop5>>> getTop5List();
}
