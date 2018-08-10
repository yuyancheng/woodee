package com.sf.kh.service;

import com.sf.kh.model.ArticleType;

import java.util.List;

/**
 * @Auther: 01378178
 * @Date: 2018/7/2 10:15
 * @Description:
 */
public interface IArticleTypeService {

     List<ArticleType> getAllArticleType();

     int addArticleType(ArticleType type);

     int updateArticleTypeById(ArticleType type);

     int deleteArticleType(Long id);
}
