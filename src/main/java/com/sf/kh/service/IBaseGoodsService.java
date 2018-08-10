package com.sf.kh.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.sf.kh.model.BaseGoods;
import com.sf.kh.model.GoodsDept;
import com.sf.kh.model.UploadFileStatus;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.Result;
/***
 * 物资管理service
 * @author 866316
 *
 */
public interface IBaseGoodsService{
	
	Result<Page<BaseGoods>> query4page(Map<String, Object> params);
	
	Result<Boolean> delete(String id);
	
	Result<Boolean> update(Long userId,BaseGoods baseGoods);
	
	Result<Boolean> add(Long userId,BaseGoods baseGoods);
	
	Result<List<BaseGoods>> getGoodsByCategoryId(String categoryId);
	
	Result<List<BaseGoods>> getAllData();
	
	Result<UploadFileStatus> uploadFile(File file,String fileName,String contentType,Long userId);
	
	File exportBaseGoods(String fileFolderPath);
	
	List<BaseGoods> getListByIds(List<Long> ids);
	
	List<Long> getListByCategoryIds(Long parentCategoryId,List<Long> categoryIds);
	
	List<GoodsDept> getGoodsDept(GoodsDept gd);
}
