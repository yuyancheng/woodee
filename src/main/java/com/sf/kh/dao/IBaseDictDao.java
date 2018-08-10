package com.sf.kh.dao;

import java.util.List;
import java.util.Map;
import com.sf.kh.model.BaseDict;

/***
 * 获取基础维表数据dao
 * @author 866316
 *
 */
public interface IBaseDictDao {
	public List<BaseDict> getListByType(String type);
	
	public List<BaseDict> getListByTypeAndObj(String type,BaseDict baseDict);
	
	public Map<Long,BaseDict> getMapByType(String type);
	
	public BaseDict insert(BaseDict baseDict);
	
	public BaseDict getBaseDictByTypeAndName(String type,String name);
	
	public List<BaseDict> getSonsByTypeAndParentId(String type,Long parentId);
	
}
