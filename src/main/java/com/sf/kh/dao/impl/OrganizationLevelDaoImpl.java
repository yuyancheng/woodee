package com.sf.kh.dao.impl;

import code.ponfee.commons.model.PageHandler;
import com.sf.kh.dao.IOrganizationLevelDao;
import com.sf.kh.dao.mapper.OrganizationLevelMapper;
import com.sf.kh.model.OrganizationLevel;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/6/20 18:08
 * @Description:
 */
@Repository
public class OrganizationLevelDaoImpl implements IOrganizationLevelDao {

    @Resource
    private OrganizationLevelMapper mapper;

    @Override
    public OrganizationLevel getById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OrganizationLevel> query4list(Map<String, Object> params) {
        PageHandler.NORMAL.handle(params);
        return mapper.query4list(params);
    }

    @Override
    public int insertOrganizationLevel(OrganizationLevel level) {
        return mapper.insert(level);
    }

    @Override
    public int updateOrganizationLevelById(OrganizationLevel level) {
        return mapper.updateByPrimaryKeySelective(level);
    }

    @Override
    public int deleteLevelById(Long id) {
        if(id == null) {
            return 0;
        }
        return mapper.deleteByPrimaryKey(id);
    }
}
