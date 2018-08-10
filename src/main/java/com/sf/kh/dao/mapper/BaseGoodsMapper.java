package com.sf.kh.dao.mapper;

import java.util.List;
import java.util.Map;
import com.sf.kh.model.BaseGoods;
import org.apache.ibatis.annotations.Param;

/***
 * 物资清单表数据
 * @author 866316
 *
 */

public interface BaseGoodsMapper {
	List<BaseGoods> query4list(Map<String, ?> params);
	
	int delete(String id);
	
	int update(BaseGoods baseGoods);
	
	int insert(List<BaseGoods> list);
	
	List<BaseGoods> getGoodsByCategoryId(String categoryId);
	
	List<BaseGoods> getAllData();
	
	/**
	 * 根据主键获取数据
	 * @param id
	 * @return
	 */
	BaseGoods getBaseGoodsById(Long id);
	/**
	 * 根据物资编码获取数据
	 * @return
	 */
	BaseGoods getGoodsByGoodsCode(String goodsCode);
	
	/***
	 * 根据主键集合获取数据
	 */
	List<BaseGoods> getBaseGoodsListByIds(List<Long> ids);

	List<Long> queryGoodsIdBySpecialtyIdAndCategoryId(@Param("specialtyId") Long specialtyId,
													  @Param("categoryId") Long categoryId,
													  @Param("parentOrgId") Long parentOrgId,
													  @Param("orgId") Long orgId);
	
	List<Long> queryGoodsIdBySpecialtyIdAndCategoryIds(@Param("specialtyId") Long specialtyId,
			  @Param("categoryIds") List<Long> categoryIds);
	
	int saveOrUpdateList(List<BaseGoods> list);
}