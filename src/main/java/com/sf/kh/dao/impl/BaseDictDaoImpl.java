package com.sf.kh.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.sf.kh.dao.IBaseDictDao;
import com.sf.kh.dao.mapper.BaseDictMapper;
import com.sf.kh.model.BaseDict;

/****
 * 基础维表dao
 * @author 866316
 *
 */
@Repository
public class BaseDictDaoImpl implements IBaseDictDao {

    private @Resource BaseDictMapper baseDictMapper;

	@Override
	public List<BaseDict> getListByType(String type) {
		return baseDictMapper.getListByType(type);
	}
	
	@Override
	public Map<Long,BaseDict> getMapByType(String type){
		List<BaseDict> tempList = baseDictMapper.getListByType(type);
		Map<Long,BaseDict>  mapList = new HashMap<Long, BaseDict>();
		for(BaseDict bd : tempList){
			mapList.put(bd.getId(), bd);
		}
		return mapList;
	}

	@Override
	public BaseDict insert(BaseDict baseDict) {
		baseDictMapper.insert(baseDict);
		return baseDict;
	}

	@Override
	public BaseDict getBaseDictByTypeAndName(String type, String name) {
		return baseDictMapper.getBaseDictByTypeAndName(type,name);
	}

	@Override
	public List<BaseDict> getSonsByTypeAndParentId(String type, Long parentId) {
		return baseDictMapper.getSonsByTypeAndParentId(type,parentId);
	}

	@Override
	public List<BaseDict> getListByTypeAndObj(String type, BaseDict baseDict) {
		return baseDictMapper.getListByTypeAndObj(type,baseDict);
	}
	
	

}
