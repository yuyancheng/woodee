package com.sf.kh.dao;

import java.util.Map;
import code.ponfee.commons.model.Page;
import com.sf.kh.model.ImportLog;

/***
 * 日志记录dao
 * @author 866316
 *
 */
public interface IImportLogDao {
	
	Page<ImportLog> query4page(Map<String, ?> params);
	
	int insert(ImportLog record);
    
    int updateById(ImportLog record);
    
	int updateByFileKey(ImportLog record);
	
	
}
