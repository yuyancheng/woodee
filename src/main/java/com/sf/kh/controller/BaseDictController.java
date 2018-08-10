package com.sf.kh.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import code.ponfee.commons.model.PageRequestParams;
import code.ponfee.commons.model.Result;

import com.sf.kh.dto.UserDto;
import com.sf.kh.dto.convert.UserConverter;
import com.sf.kh.model.BaseDict;
import com.sf.kh.model.User;
import com.sf.kh.service.IBaseDictService;
import com.sf.kh.service.IDepartmentService;
import com.sf.kh.service.IUserService;
import com.sf.kh.util.WebContextHolder;


/***
 * 维表数据管理
 * @author 866316
 *
 */
@RestController
@RequestMapping(path = "baseDict")
public class BaseDictController {
	 private @Resource IBaseDictService baseDictService;
	 private @Resource IUserService userService;
	 private @Resource IDepartmentService deptService;
	/***
	 * 获取物资类型
	 * @param params
	 * @return
	 */
	@PostMapping(path = "getGoodsTypes")
    public Result<List<BaseDict>> getGoodsTypes(HttpSession session,PageRequestParams params) {
        return baseDictService.getGoodsTypes();
    }
	
	/***
	 * 获取物资类型--与态势和统计有关系
	 * @param params
	 * @return
	 */
	@PostMapping(path = "getGoodsDataByStatistics")
    public Result<List<BaseDict>> getGoodsDataByStatistics(HttpSession session,PageRequestParams params) {
		User user = WebContextHolder.currentUser();
        if (user.getDeptId() != null) {
            user.setDept(deptService.getDeptWithHierarchicalOrgById(user.getDeptId()));
        }
        user.setRoles(userService.queryUserRoles(user.getId()).getData());
        UserDto userDto = new UserConverter().convert(user);
        return baseDictService.getGoodsDataByStatistics(userDto);
    }
	
	/***
	 * 获取物资维表集合
	 * @param params
	 * @return
	 */
	@PostMapping(path = "getGoodsData")
    public Result<List<BaseDict>> getGoodsData(HttpSession session,PageRequestParams params) {
        return baseDictService.getGoodsData();
    }
	
	/***
	 * 获取当前登录用户的专业或类别
	 * @param session
	 * @param params
	 * @return
	 */
	@PostMapping(path = "getCategoryInfo")
    public Result<Map<String,String>> getCategoryInfo(HttpSession session) {
		User user = WebContextHolder.currentUser();
        if (user.getDeptId() != null) {
            user.setDept(deptService.getDeptWithHierarchicalOrgById(user.getDeptId()));
        }
        user.setRoles(userService.queryUserRoles(user.getId()).getData());
        UserDto userDto = new UserConverter().convert(user);
        return baseDictService.getCategoryInfo(userDto);
    }
}
