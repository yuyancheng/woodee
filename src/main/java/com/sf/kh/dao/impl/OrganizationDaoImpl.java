package com.sf.kh.dao.impl;

import code.ponfee.commons.model.PageHandler;
import com.sf.kh.dao.IOrganizationDao;
import com.sf.kh.dao.mapper.OrganizationMapper;
import com.sf.kh.model.Organization;
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
public class OrganizationDaoImpl implements IOrganizationDao {

    @Resource
    private OrganizationMapper organizationMapper;

    @Override
    public Organization getById(Long id) {
        return organizationMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Organization> query4list(Map<String, ?> params) {
        PageHandler.NORMAL.handle(params);
        return organizationMapper.query4list(params);
    }

    @Override
    public int insertOrganization(Organization org) {
        return organizationMapper.insert(org);
    }

    @Override
    public int updateOrganizationById(Organization org) {
        return organizationMapper.updateByPrimaryKeySelective(org);
    }

    @Override
    public List<String> listOrgNameByOrgLevelName(long orgTypeId, String levelName) {
        return organizationMapper.listOrgNameByOrgLevelName(orgTypeId, levelName);
    }

}
