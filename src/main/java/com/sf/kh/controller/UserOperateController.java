package com.sf.kh.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sf.kh.dto.PermitFlat;
import com.sf.kh.dto.UserDto;
import com.sf.kh.dto.UserReceiveDto;
import com.sf.kh.model.Department;
import com.sf.kh.model.Role;
import com.sf.kh.model.User;
import com.sf.kh.service.IDepartmentService;
import com.sf.kh.service.IUserService;
import com.sf.kh.util.CommonUtils;
import com.sf.kh.util.WebContextHolder;

import code.ponfee.commons.collect.Collects;
import code.ponfee.commons.model.AbstractDataConverter;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import code.ponfee.commons.util.RegexUtils;

/**
 * The spring mvc controller for t_user CRUD operators
 * 
 * user self operate
 * 
 * @author 01367825
 */
@RestController
@RequestMapping("user/ops")
public class UserOperateController {

    private @Resource IUserService userService;
    private @Resource IDepartmentService deptService;

    /**
     * Modify info: mobilePhone, nickname, password
     * 
     * @param user
     * @return
     */
    @PostMapping("modifyinfo")
    public Result<Void> modifyInfo(@RequestBody User user) {
        if (StringUtils.isAllBlank(user.getNickname(), user.getMobilePhone(), user.getPassword())) {
            return Result.failure(ResultCode.BAD_REQUEST, "请填写要修改的用户信息");
        }

        if (StringUtils.isNotBlank(user.getPassword())) {
            user.setPassword(passwd(user.getPassword(), user.getOriginPassword()));
        }

        long userId = WebContextHolder.currentUser().getId();
        user.setId(userId);
        user.setUpdateBy(userId);
        user.setPrompt(false);
        return userService.modifyInfo(user);
    }

    @PostMapping("modifyname")
    public Result<Void> modifyname(@RequestBody Map<String, Object> map) {
        String nickname = (String) map.get("nickname");
        if (StringUtils.isBlank(nickname)) {
            return Result.failure(ResultCode.BAD_REQUEST, "姓名不能为空");
        }
        long userId = WebContextHolder.currentUser().getId();
        User user = new User();
        user.setId(userId);
        user.setNickname(nickname);
        user.setUpdateBy(userId);
        user.setPrompt(false);
        return userService.modifyInfo(user);
    }

    @PostMapping("modifyphone")
    public Result<Void> modifyphone(@RequestBody Map<String, Object> map) {
        String mobilePhone = (String) map.get("mobilePhone");
        if (StringUtils.isBlank(mobilePhone)) {
            return Result.failure(ResultCode.BAD_REQUEST, "手机号码不能为空");
        }
        long userId = WebContextHolder.currentUser().getId();
        User user = new User();
        user.setId(userId);
        user.setMobilePhone(mobilePhone);
        user.setUpdateBy(userId);
        user.setPrompt(false);
        return userService.modifyInfo(user);
    }

    @PostMapping("modifypwd")
    public Result<Void> modifypwd(@RequestBody Map<String, Object> map) {
        long userId = WebContextHolder.currentUser().getId();
        User user = new User();
        user.setId(userId);
        user.setPassword(passwd((String) map.get("password"), (String) map.get("originPassword")));
        user.setUpdateBy(userId);
        user.setPrompt(false);
        return userService.modifyInfo(user);
    }

    /**
     * Modify dept address: province, city, area, addresDetail
     * 
     * @param dept
     * @return
     */
    @PostMapping("modifyaddr")
    public Result<Void> modifyAddr(@RequestBody Department dept) {
        User user = WebContextHolder.currentUser();
        if (user.getDeptId() == null || user.getDeptId() < 1) {
            return Result.failure(ResultCode.OPS_CONFLICT, "无法修改地址：还未分配部门");
        }
        Department d = new Department(user.getDeptId());
        d.setProvinceName(dept.getProvinceName());
        d.setCityName(dept.getCityName());
        d.setAreaName(dept.getAreaName());
        d.setAddressDetail(dept.getAddressDetail());
        d.setUpdateBy(user.getId());
        deptService.updateDept(d);
        return Result.SUCCESS;
    }

    /**
     * Get my role info
     * 
     * @return
     */
    @GetMapping("myrole")
    public Result<Role> myRole() {
        return AbstractDataConverter.convertResultBean(
            userService.queryUserRoles(WebContextHolder.currentUser().getId()), 
            roles -> CollectionUtils.isEmpty(roles) ? null : roles.get(0)
        );
    }

    /**
     * Get dept addr
     * 
     * @return
     */
    @GetMapping("myaddr")
    public Result<Map<String, Object>> myAddr() {
        User user = WebContextHolder.currentUser();
        Department dept = null;
        if (user.getDeptId() != null) {
            dept = deptService.getDeptWithHierarchicalOrgById(user.getDeptId());
        }
        // Lambda expression's parameter dept cannot redeclare another local variable defined in an enclosing scope.
        return Result.success(AbstractDataConverter.convert(dept, 
            t -> Collects.ofMap(t, "provinceName", "cityName", "areaName", "addressDetail"))
        );
    }

    /**
     * Get my info
     * 
     * @return
     */
    @GetMapping("myinfo")
    public Result<UserDto> myInfo() {
        return Result.success(WebContextHolder.currentUserDto());
    }

    @GetMapping("mypermits")
    public Result<List<PermitFlat>> mypermits() {
        User user = WebContextHolder.currentUser();
        return userService.queryUserPermitsAsFlat(user.getId());
    }

    @PostMapping("receiveUserInfo")
    public Result<UserReceiveDto> receiveUserInfo(@RequestBody Map<String, Object> map) {
        Object obj = map.get("deptId");
        if (null == obj) {
            return Result.failure(ResultCode.BAD_REQUEST, "为获取到单位编号");
        }
        return userService.getUserInfoByDeptId(String.valueOf(obj));
    }

    private String passwd(String password, String originPassword) {
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("密码不能为空");
        }
        password = CommonUtils.decryptPassword(password);
        if (!RegexUtils.isValidPassword(password)) {
            throw new IllegalArgumentException("密码格式错误");
        }

        if (StringUtils.isBlank(originPassword)) {
            throw new IllegalArgumentException("原密码不能为空");
        }
        originPassword = CommonUtils.decryptPassword(originPassword);
        User user = WebContextHolder.currentUser();
        if (!CommonUtils.checkPassword(originPassword, user.getPassword())) {
            throw new IllegalArgumentException("原密码错误");
        }

        return CommonUtils.cryptPassword(password);
    }
}
