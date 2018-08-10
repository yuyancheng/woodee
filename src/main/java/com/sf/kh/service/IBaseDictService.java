package com.sf.kh.service;

import java.util.List;
import java.util.Map;

import code.ponfee.commons.model.Result;

import com.sf.kh.dto.UserDto;
import com.sf.kh.model.BaseDict;


/***
 * 物资管理service
 * @author 866316
 *
 */
public interface IBaseDictService {
	public Result<List<BaseDict>> getGoodsTypes();
	
	public Result<List<BaseDict>> getGoodsData();
	
	public Result<List<BaseDict>> getGoodsDataByStatistics(UserDto userDto);
	
	public Result<Map<String,String>> getCategoryInfo(UserDto userDto);
}
