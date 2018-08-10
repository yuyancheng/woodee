package com.sf.kh.dao;

import com.sf.kh.model.ArticleType;

import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/7/2 13:56
 * @Description:
 */
public interface IArticleTypeDao {

    ArticleType getById(Long id);

    List<ArticleType> query4list(Map<String, ?> params);

    int insertArticleType(ArticleType type);

    int updateArticleTypeById(ArticleType type);

    int deleteArticleTypeById(Long id);

}
