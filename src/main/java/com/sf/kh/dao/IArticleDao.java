package com.sf.kh.dao;

import com.sf.kh.model.Article;
import com.sf.kh.model.ArticleType;
import com.sf.kh.model.BaseDict;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/7/2 10:22
 * @Description:
 */
@Repository
public interface IArticleDao {

    Article getById(Long id);

    List<Article> query4list(Map<String, ?> params);

    int insertArticle(Article article);

    int updateArticleById(Article article);

    int deleteArticleById(Long id);

    int deleteArticleBatch(List<Long> ids);

    List<Article> query4listWithContent(Map<String, ?> params);

}