package com.sf.kh.dao;

import java.util.List;
import com.sf.kh.model.IndexRankTop5;

/***
 * 获取top5首页记录
 * @author 866316
 *
 */
public interface IIndexRankTop5Dao {
	
	List<IndexRankTop5> getTop5Result(String type,String version);
	
}
