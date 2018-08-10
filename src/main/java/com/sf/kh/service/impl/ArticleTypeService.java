package com.sf.kh.service.impl;

import com.google.common.collect.Maps;
import com.sf.kh.dao.IArticleTypeDao;
import com.sf.kh.model.ArticleType;
import com.sf.kh.service.IArticleTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/7/2 13:59
 * @Description:
 */
@Service
public class ArticleTypeService implements IArticleTypeService {

    @Resource
    private IArticleTypeDao articleTypeDao;

    @Override
    public List<ArticleType> getAllArticleType() {
        Map<String, Object> params = Maps.newHashMap();
        return articleTypeDao.query4list(params);
    }

    @Override
    public int addArticleType(ArticleType type) {
        if(type == null){
            return 0;
        }
        return articleTypeDao.insertArticleType(type);
    }

    @Override
    public int updateArticleTypeById(ArticleType type) {
        if(type==null || type.getId() == null) {
            return 0;
        }
        return articleTypeDao.updateArticleTypeById(type);
    }

    @Override
    public int deleteArticleType(Long id) {
        if(id == null){
            return 0;
        }
        return articleTypeDao.deleteArticleTypeById(id);
    }
}
