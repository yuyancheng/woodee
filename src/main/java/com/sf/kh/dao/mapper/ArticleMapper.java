package com.sf.kh.dao.mapper;


import com.sf.kh.model.Article;

import java.util.List;
import java.util.Map;

public interface ArticleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Article record);

    int updateByPrimaryKeySelective(Article record);

    List<Article> query4list(Map<String, ?> params);

    Article selectByPrimaryKey(Long id);

    int insertSelective(Article record);

    int updateByPrimaryKey(Article record);

    int batchDeleteByPrimaryKey(List<Long> ids);

    List<Article> query4listWithContent(Map<String, ?> params);
}