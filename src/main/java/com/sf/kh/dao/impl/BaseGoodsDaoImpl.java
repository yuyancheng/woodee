package com.sf.kh.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sf.kh.dao.IBaseGoodsDao;
import com.sf.kh.dao.mapper.BaseGoodsMapper;
import com.sf.kh.model.BaseGoods;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageHandler;

/****
 * 物资管理dao
 * @author 866316
 *
 */
@Repository
public class BaseGoodsDaoImpl implements IBaseGoodsDao {

    private @Resource BaseGoodsMapper baseGoodsMapper;
    
    private @Resource JdbcTemplate jdbcTemplate;
    

	@Override
	public  Page<BaseGoods> query4page(Map<String, ?> params) {
		PageHandler.NORMAL.handle(params);
        return new Page<>(baseGoodsMapper.query4list(params));
	}

	@Override
	public int delete(String id) {
		return baseGoodsMapper.delete(id);
	}

	@Override
	public int update(BaseGoods baseGoods) {
		return baseGoodsMapper.update(baseGoods);
	}

	@Override
	public int insert(List<BaseGoods> list){
		return baseGoodsMapper.insert(list);
	}

	@Override
	public List<BaseGoods> getGoodsByCategoryId(String categoryId) {
		return baseGoodsMapper.getGoodsByCategoryId(categoryId);
	}
	
	@Override
	public List<BaseGoods> getAllData() {
		return baseGoodsMapper.getAllData();
	}

	@Override
	public BaseGoods getBaseGoodsById(Long id) {
		return baseGoodsMapper.getBaseGoodsById(id);
	}
	
	@Override
	public BaseGoods getGoodsByGoodsCode(String goodsCode){
		return baseGoodsMapper.getGoodsByGoodsCode(goodsCode);
	}
	
	@Override
	public List<Map<String, Object>> executeSelectSql(String sql){
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<BaseGoods> getBaseGoodsByIds(List<Long> ids) {
		return baseGoodsMapper.getBaseGoodsListByIds(ids);
	}

	@Override
	public List<Long> queryGoodsIdBySpecialtyIdAndCategoryId(Long specialtyId, Long categoryId, Long parentOrgId, Long orgId) {
		return baseGoodsMapper.queryGoodsIdBySpecialtyIdAndCategoryId(specialtyId, categoryId, parentOrgId, orgId);
	}

	@Override
	public List<Long> queryGoodsIdBySpecialtyIdAndCategoryIds(Long specialtyId,
			List<Long> categoryIds) {
		return baseGoodsMapper.queryGoodsIdBySpecialtyIdAndCategoryIds(specialtyId, categoryIds);
	}

	@Override
	public int saveOrUpdateList(List<BaseGoods> list) {
		return baseGoodsMapper.saveOrUpdateList(list);
	}

}
