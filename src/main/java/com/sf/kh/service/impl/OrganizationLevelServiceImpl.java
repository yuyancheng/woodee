package com.sf.kh.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sf.kh.dao.IOrganizationLevelDao;
import com.sf.kh.model.OrganizationLevel;
import com.sf.kh.service.IOrganizationLevelService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/6/20 18:05
 * @Description:
 */
@Service
public class OrganizationLevelServiceImpl implements IOrganizationLevelService {

    @Resource
    private IOrganizationLevelDao organizationLevelDao;

    @Override
    public int addOrganizationLevel(OrganizationLevel level) {
        return organizationLevelDao.insertOrganizationLevel(level);
    }

    @Override
    public OrganizationLevel getOrganizationLevelById(Long id) {
        return organizationLevelDao.getById(id);
    }


    @Override
    public List<OrganizationLevel> getOrgLevelByOrgTypeId(Long orgTypeId) {
        Map<String, Object> params = ImmutableMap.of("orgTypeId", orgTypeId);
        return organizationLevelDao.query4list(params);
    }

    @Override
    public OrganizationLevel getHierarchicalOrgLevel(Long orgTypeId){
        return buildDescendingOrgLevel(getOrgLevelByOrgTypeId(orgTypeId));
    }

    @Override
    public OrganizationLevel getSubOrgLevel(Long levelId) {
        Map<String, Object> params = ImmutableMap.of("parentLevelId",levelId);
        List<OrganizationLevel> level = organizationLevelDao.query4list(params);
        if(CollectionUtils.isEmpty(level)){
            return null;
        }
        return level.get(0);
    }

    @Override
    public List<OrganizationLevel> list(Map<String, Object> params) {
        return organizationLevelDao.query4list(params);
    }


    public OrganizationLevel buildDescendingOrgLevel(List<OrganizationLevel> orgLevels){

        if(CollectionUtils.isEmpty(orgLevels)){
            return null;
        }

        Map<Long, OrganizationLevel> maps = Maps.newHashMap();
        OrganizationLevel topLevel = null;
        for(OrganizationLevel orgLevel : orgLevels){
            maps.put(orgLevel.getId(), orgLevel);
            if(orgLevel.getParentLevelId().equals(0L)){
                topLevel = orgLevel;
            }
        }

        for(OrganizationLevel orgLevel : orgLevels){

            // top org level
            if(!orgLevel.getParentLevelId().equals(0L)){
                if(maps.get(orgLevel.getParentLevelId())!=null){
                    maps.get(orgLevel.getParentLevelId()).setSubLevel(orgLevel);
                }
            }
        }

        return topLevel;
    }

    @Override
    public int deleteOrgLevelById(Long id) {
        if(id == null) {
            return 0;
        }
        return organizationLevelDao.deleteLevelById(id);
    }

    /**
     * 根据组织类型获取组织层级名称, 从顶到底排序
     *
     * @param orgTypeId
     * @return
     */
    public List<String> getDescendingOrgLevelName(Long orgTypeId) {

        OrganizationLevel topLevel = getHierarchicalOrgLevel(orgTypeId);

        List<String> flexHeaders = Lists.newArrayList();
        while (topLevel != null) {
            flexHeaders.add(topLevel.getLevelName());
            topLevel = topLevel.getSubLevel();
        }

        return flexHeaders;
    }

}
