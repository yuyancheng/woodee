package com.sf.kh.dao.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;
import com.sf.kh.dao.IIndexRankTop5Dao;
import com.sf.kh.dao.mapper.IndexRankTop5Mapper;
import com.sf.kh.model.IndexRankTop5;

/****
 * 首页top5的dao
 * @author 866316
 *
 */
@Repository
public class IndexRankTop5DaoImpl implements IIndexRankTop5Dao {

    private @Resource IndexRankTop5Mapper indexRankTop5Mapper;
    
    @Override
    public List<IndexRankTop5> getTop5Result(String type,String version){
    	return indexRankTop5Mapper.getTop5Result(type,version);
    }
}
