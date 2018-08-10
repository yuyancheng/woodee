package com.sf.kh.dao.impl;

import code.ponfee.commons.model.PageHandler;
import com.sf.kh.dao.IArticleDao;
import com.sf.kh.dao.mapper.ArticleMapper;
import com.sf.kh.model.Article;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/7/2 14:53
 * @Description:
 */
@Repository
public class ArticleDaoImpl implements IArticleDao {

    @Resource
    private ArticleMapper mapper;


    @Override
    public Article getById(Long id) {
        if(id == null){
            return null;
        }
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Article> query4list(Map<String, ?> params) {
        PageHandler.NORMAL.handle(params);
        return mapper.query4list(params);
    }

    @Override
    public int insertArticle(Article article) {
        if(article==null) {
            return 0;
        }
        return mapper.insert(article);
    }

    @Override
    public int updateArticleById(Article article) {
        if(article == null || article.getId() == null) {
            return 0;
        }
        return mapper.updateByPrimaryKeySelective(article);
    }

    @Override
    public int deleteArticleById(Long id) {
        if(id==null){
            return 0;
        }
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteArticleBatch(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return 0;
        }

        return mapper.batchDeleteByPrimaryKey(ids);
    }

    @Override
    public List<Article> query4listWithContent(Map<String, ?> params) {
        PageHandler.NORMAL.handle(params);
        return mapper.query4listWithContent(params);
    }
}
