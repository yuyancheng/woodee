package com.sf.kh.service.impl;

import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.sf.kh.dao.IImportLogDao;
import com.sf.kh.model.ImportLog;
import com.sf.kh.service.IImportLogService;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.Result;

/***
 * 存储日志记录
 * @author 866316
 *
 */
@Service
public class ImportLogServiceImpl implements IImportLogService {

    private @Resource IImportLogDao importLogDao;

	@Override
	public Boolean insert(ImportLog importLog) {
		int result = importLogDao.insert(importLog);
		if(result>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Boolean updateById(ImportLog importLog) {
		int result = importLogDao.updateById(importLog);
		if(result>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Boolean updateByFileKey(ImportLog importLog) {
		int result = importLogDao.updateByFileKey(importLog);
		if(result>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Result<Page<ImportLog>> query4page(Map<String, Object> params) {
		return Result.success(importLogDao.query4page(params));
	}
		
	
}
