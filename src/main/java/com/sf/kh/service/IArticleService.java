package com.sf.kh.service;

import com.sf.kh.model.Article;

import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/6/27 15:33
 * @Description:
 */
public interface IArticleService {

    Article getById(Long id);

    int addArticle(Article article);

    int updateArticleTypeById(Article type);

    int deleteArticleType(Long id);

    List<Article> list(Map<String, Object> param);

    int deleteArticleBatch(List<Long> ids);

    List<Article> listWithContent(Map<String, Object> param);
}
