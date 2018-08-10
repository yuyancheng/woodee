package com.sf.kh.controller;

import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import com.sf.kh.model.OrganizationLevel;
import com.sf.kh.service.IOrganizationLevelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Auther: 01378178
 * @Date: 2018/6/15 15:22
 * @Description:
 */
@RestController
@RequestMapping("organizationLevel")
public class OrganizationLevelController {

    static final Logger logger = LoggerFactory.getLogger(OrganizationLevelController.class);

    @Resource
    private IOrganizationLevelService organizationLevelService;

    /**
     *updateOrg
     * @param session
     * @param orgTypeId
     * @return
     */
    @GetMapping(path="getHierarchicalOrgLevelByOrgTypeId")
    public Result<OrganizationLevel> getTopOrgLevelByOrgTypeId(@RequestParam(value = "orgTypeId", required = true) Long orgTypeId){

        if(orgTypeId==null){
            return Result.failure(ResultCode.BAD_REQUEST, "组织类型id不能为空");
        }

        OrganizationLevel org = organizationLevelService.getHierarchicalOrgLevel(orgTypeId);
        return Result.success(org);

    }

    @GetMapping(path="getSubLevel")
    public Result<OrganizationLevel> getSubLevel(@RequestParam(value = "levelId", required = true) Long levelId){
        if(levelId==null){
            return Result.failure(ResultCode.BAD_REQUEST, "组织级别id不能为空");
        }
       return  Result.success(organizationLevelService.getSubOrgLevel(levelId));
    }

}
