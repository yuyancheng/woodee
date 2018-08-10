package com.sf.kh.dao.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Repository;
import com.sf.kh.dao.IIndexRankTop5VersionDao;
import com.sf.kh.dao.mapper.IndexRankTop5VersionMapper;
import com.sf.kh.model.IndexRankTop5Version;

/****
 * 首页top5版本号记录
 * @author 866316
 *
 */
@Repository
public class IndexRankTop5VersionDaoImpl implements IIndexRankTop5VersionDao {

    private @Resource IndexRankTop5VersionMapper indexRankTop5VersionMapper;
    

	@Override
	public IndexRankTop5Version getNewestVersionRecord() {
		return indexRankTop5VersionMapper.getNewestVersionRecord();
	}
	
}
