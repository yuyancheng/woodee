package com.sf.kh.service;

import java.util.Map;
import com.sf.kh.model.ImportLog;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.Result;
/***
 * 存储导入的日志记录
 * @author 866316
 *
 */
public interface IImportLogService{
	
	Result<Page<ImportLog>> query4page(Map<String, Object> params);
	
	Boolean insert(ImportLog importLog);
	
	Boolean updateById(ImportLog importLog);
	
	Boolean updateByFileKey(ImportLog importLog);
}
