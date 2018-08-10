package com.sf.kh.dao.mapper;


import com.sf.kh.model.ArticleType;

import java.util.List;
import java.util.Map;

public interface ArticleTypeMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ArticleType record);

    int insertSelective(ArticleType record);

    ArticleType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ArticleType record);

    int updateByPrimaryKey(ArticleType record);

    List<ArticleType> query4list(Map<String, ?> params);
}