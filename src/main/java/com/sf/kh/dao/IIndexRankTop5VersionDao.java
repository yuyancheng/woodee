package com.sf.kh.dao;

import com.sf.kh.model.IndexRankTop5Version;

/***
 * 首页top5版本号记录
 * @author 866316
 *
 */
public interface IIndexRankTop5VersionDao {
	
	IndexRankTop5Version getNewestVersionRecord();
	
}
