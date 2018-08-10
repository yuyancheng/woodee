package com.sf.kh.dao.impl;

import code.ponfee.commons.model.PageHandler;
import com.sf.kh.dao.IOrganizationTypeDao;
import com.sf.kh.dao.mapper.OrganizationTypeMapper;
import com.sf.kh.model.OrganizationType;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/6/14 16:40
 * @Description:
 */

@Repository
public class OrganizationTypeDaoImpl implements IOrganizationTypeDao {

    @Resource
    private OrganizationTypeMapper organizationTypeMapper;


    @Override
    public OrganizationType getById(Long id) {
        return organizationTypeMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OrganizationType> query4list(Map<String, ?> params) {
//        PageHandler.NORMAL.handle(params);
        return organizationTypeMapper.query4list(params);
    }

    @Override
    public int insertOrganizationType(OrganizationType orgType) {
        return organizationTypeMapper.insertSelective(orgType);
    }

    @Override
    public int updateOrganizationTypeById(OrganizationType orgType) {
        return organizationTypeMapper.updateByPrimaryKeySelective(orgType);
    }
}
