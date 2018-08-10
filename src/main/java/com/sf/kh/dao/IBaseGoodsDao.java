package com.sf.kh.dao;

import java.util.List;
import java.util.Map;

import code.ponfee.commons.model.Page;

import com.sf.kh.model.BaseGoods;

/***
 * 物资管理dao
 * @author 866316
 *
 */
public interface IBaseGoodsDao {
	Page<BaseGoods> query4page(Map<String, ?> params);
	
	int delete(String id);
	
	int update(BaseGoods baseGoods);
	
	int insert(List<BaseGoods> list);
	
	List<BaseGoods> getAllData();
	
	List<BaseGoods> getGoodsByCategoryId(String categoryId);
	
	BaseGoods getGoodsByGoodsCode(String goodsCode);
	
	BaseGoods getBaseGoodsById(Long id);
	
	List<Map<String, Object>> executeSelectSql(String sql);
	
	List<BaseGoods> getBaseGoodsByIds(List<Long> ids);

	List<Long> queryGoodsIdBySpecialtyIdAndCategoryId(Long specialtyId, Long categoryId, Long parentOrgId, Long orgId);
	
	List<Long> queryGoodsIdBySpecialtyIdAndCategoryIds(Long specialtyId, List<Long> categoryIds);
	
	int saveOrUpdateList(List<BaseGoods> list);
}
