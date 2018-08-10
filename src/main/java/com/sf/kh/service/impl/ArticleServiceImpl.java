package com.sf.kh.service.impl;

import code.ponfee.commons.constrain.Constraint;
import code.ponfee.commons.log.LogAnnotation;
import code.ponfee.commons.model.PageHandler;
import com.google.common.collect.Maps;
import com.sf.kh.dao.IArticleDao;
import com.sf.kh.model.Article;
import com.sf.kh.model.ArticleType;
import com.sf.kh.service.IArticleService;
import com.sf.kh.service.IArticleTypeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/7/2 10:20
 * @Description:
 */
@Service
public class ArticleServiceImpl implements IArticleService {

    @Resource
    private IArticleDao articleDao;

    @Override
    public Article getById(Long id) {
        if(id==null){
            return null;
        }
        return articleDao.getById(id);
    }

    @LogAnnotation(type = LogAnnotation.LogType.ADD)
    @Constraint(field = "title", notBlank = true, maxLen = 50, msg = "标题不能为空或超长")
    @Constraint(field = "typeId", notNull = true, msg = "文章类型id不能为空")
    @Override
    public int addArticle(Article article) {
        if(article == null){
            return 0;
        }
        return articleDao.insertArticle(article);
    }

    @Override
    public int updateArticleTypeById(Article article) {
        if(article == null || article.getId() == null){
            return 0;
        }
        return articleDao.updateArticleById(article);
    }

    @Override
    public int deleteArticleType(Long id) {
        if(id == null){
            return 0;
        }
        return articleDao.deleteArticleById(id);
    }

    @Override
    public List<Article> list(Map<String, Object> params) {
        return articleDao.query4list(params);
    }

    @Override
    public int deleteArticleBatch(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return 0;
        }
        return articleDao.deleteArticleBatch(ids);
    }

    @Override
    public List<Article> listWithContent(Map<String, Object> params) {
        return articleDao.query4listWithContent(params);
    }
}
