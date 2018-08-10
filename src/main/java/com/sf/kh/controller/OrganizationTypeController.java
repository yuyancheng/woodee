package com.sf.kh.controller;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import com.google.common.collect.ImmutableMap;
import com.sf.kh.exception.BusinessException;
import com.sf.kh.model.OrganizationType;
import com.sf.kh.model.User;
import com.sf.kh.model.ValidStatusEnum;
import com.sf.kh.service.IOrganizationTypeService;
import com.sf.kh.util.WebContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/6/15 10:17
 * @Description:departmentdepartment
 */
@RestController
@RequestMapping(path = "organizationType")
public class OrganizationTypeController {

    @Resource
    private IOrganizationTypeService organizationTypeService;

    @PostMapping(path = "addOrgType")
    public Result<Map> addOrganizationType(@RequestBody OrganizationType orgType) {

        if(StringUtils.isBlank(orgType.getOrgTypeName())){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "名称不能为空");
        }
        if(orgType.getOrgTypeName().length() > 50){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(),"名称不能超过50个字符");
        }


        Date date = new Date();
        User user = WebContextHolder.currentUser();
        orgType.setValid( ValidStatusEnum.VALID.getCode());
        orgType.setCreateBy(user==null ? null : user.getId());
        orgType.setCreateTm(date);
        orgType.setUpdateBy(user==null ? null : user.getId());
        orgType.setUpdateTm(date);

        int rs =  organizationTypeService.addOrgType(orgType);
        if(rs!=1){
            return Result.failure(ResultCode.SERVER_ERROR, "更新失败, 请重试");
        }

        return Result.success(ImmutableMap.of("id", orgType.getId()));
    }


    @PostMapping(path = "updateOrgType")
    public Result<Void> updateOrgType( @RequestBody OrganizationType orgType) {

        Date date = new Date();
        User user = WebContextHolder.currentUser();
        orgType.setUpdateBy(user==null ? null : user.getId());
        orgType.setUpdateTm(date);

        if(orgType.getId() == null){
            return Result.failure(ResultCode.BAD_REQUEST, "id不能为空");
        }

        if(StringUtils.isNotBlank(orgType.getOrgTypeName()) && orgType.getOrgTypeName().length() > 50){
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(),"名称不能超过50个字符");
        }

        int rs = organizationTypeService.updateOrgType(orgType);
        if(rs!=1){
            return Result.failure(ResultCode.SERVER_ERROR, "更新失败, 请重试");
        }

        return Result.success();
    }

    @PostMapping(path = "disableOrgType")
    public Result<Void> disableOrganizationType(@RequestBody OrganizationType orgType){

        Date date = new Date();
        User user = WebContextHolder.currentUser();
        orgType.setUpdateBy(user==null ? null : user.getId());
        orgType.setUpdateTm(date);

        if(orgType.getId() == null){
            return Result.failure(ResultCode.BAD_REQUEST, "id不能为空");
        }

        organizationTypeService.disableOrgType(orgType);

        return Result.success();


    }

    @GetMapping(path = "getAllValidOrgTypes")
    public Result<Page<List<OrganizationType>>> getAllValidOrgTypes(){

        List<OrganizationType> orgTypes =  organizationTypeService.getAllValidOrgType();
        return Result.success(new Page(orgTypes));
    }


}
