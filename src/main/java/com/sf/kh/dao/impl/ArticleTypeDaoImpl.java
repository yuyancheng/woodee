package com.sf.kh.dao.impl;

import code.ponfee.commons.model.PageHandler;
import com.sf.kh.dao.IArticleTypeDao;
import com.sf.kh.dao.mapper.ArticleTypeMapper;
import com.sf.kh.model.ArticleType;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/7/2 13:51
 * @Description:
 */
@Repository
public class ArticleTypeDaoImpl implements IArticleTypeDao {

    @Resource
    private ArticleTypeMapper mapper;

    @Override
    public ArticleType getById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ArticleType> query4list(Map<String, ?> params) {
        PageHandler.NORMAL.handle(params);
        return mapper.query4list(params);
    }
    @Override
    public int insertArticleType(ArticleType org) {
        return mapper.insert(org);
    }

    @Override
    public int updateArticleTypeById(ArticleType org) {
        return mapper.updateByPrimaryKey(org);
    }

    @Override
    public int deleteArticleTypeById(Long id) {
        return mapper.deleteByPrimaryKey(id);
    }
}
