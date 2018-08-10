package com.sf.kh.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sf.kh.dao.IBaseGoodsDao;
import com.sf.kh.dao.IImportLogDao;
import com.sf.kh.dao.mapper.BaseGoodsMapper;
import com.sf.kh.dao.mapper.ImportLogMapper;
import com.sf.kh.model.BaseGoods;
import com.sf.kh.model.ImportLog;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageHandler;

/****
 * 日志dao
 * @author 866316
 *
 */
@Repository
public class ImportLogDaoImpl implements IImportLogDao {

    private @Resource ImportLogMapper importLogMapper;
    

	@Override
	public  Page<ImportLog> query4page(Map<String, ?> params) {
		PageHandler.NORMAL.handle(params);
        return new Page<>(importLogMapper.query4list(params));
	}
	
	@Override
	public int insert(ImportLog record){
		return importLogMapper.insert(record);
	}
    
	@Override
    public int updateById(ImportLog record){
    	return importLogMapper.updateById(record);
    }
    
	@Override
    public int updateByFileKey(ImportLog record){
    	return importLogMapper.updateByFileKey(record);
    }
	
}
