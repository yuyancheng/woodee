package com.sf.kh.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import code.ponfee.commons.model.Result;
import com.sf.kh.dao.IIndexRankTop5Dao;
import com.sf.kh.dao.IIndexRankTop5VersionDao;
import com.sf.kh.model.IndexRankTop5;
import com.sf.kh.model.IndexRankTop5Version;
import com.sf.kh.service.IIndexRankTop5Service;

/***
 * 获取首页top5的记录
 * @author 866316
 *
 */
@Service
public class IndexRankTop5ServiceImpl implements IIndexRankTop5Service {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexRankTop5ServiceImpl.class);

	private @Resource IIndexRankTop5Dao indexRankTop5Dao;
    
    private @Resource IIndexRankTop5VersionDao indexRankTop5VersionDao;
    
	@Override
	public Result<Map<String, List<IndexRankTop5>>> getTop5List() {
		IndexRankTop5Version indexRankTop5Version = indexRankTop5VersionDao.getNewestVersionRecord();
		if(null == indexRankTop5Version){
			return Result.success(this.getData(null,null,null,null));
		}
		String version = String.valueOf(indexRankTop5Version.getVersion());
		if(StringUtils.isBlank(version)){
			return Result.success(this.getData(null,null,null,null));
		}
		logger.info("当前首页top5版本号：{}",version);
		
		return Result.success((this.getData(indexRankTop5Dao.getTop5Result("1", version),
				indexRankTop5Dao.getTop5Result("2", version),
				indexRankTop5Dao.getTop5Result("3", version),
				indexRankTop5Dao.getTop5Result("4", version))));
	}
	
	private Map<String,List<IndexRankTop5>> getData(List<IndexRankTop5> a,
			List<IndexRankTop5> b,List<IndexRankTop5> c,List<IndexRankTop5> d){
		Map<String,List<IndexRankTop5>> result = new HashMap<String,List<IndexRankTop5>>();
		result.put("sendDept", a==null?new ArrayList<IndexRankTop5>():a);
		result.put("sendMaterial", b==null?new ArrayList<IndexRankTop5>():b);
		result.put("receiveDept", c==null?new ArrayList<IndexRankTop5>():c);
		result.put("receiveArea", d==null?new ArrayList<IndexRankTop5>():d);
	    return result;
	}
}
