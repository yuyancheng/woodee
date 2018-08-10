package com.sf.kh.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageRequestParams;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import code.ponfee.commons.util.SpringContextHolder;

import com.sf.kh.dto.CategoryStatisticsDto;
import com.sf.kh.dto.DepartmentStatisticsDto;
import com.sf.kh.dto.GoodsStatisticsDetailDto;
import com.sf.kh.dto.OverviewVersionDataDto;
import com.sf.kh.dto.TimeStatisticsDto;
import com.sf.kh.dto.UserDto;
import com.sf.kh.dto.convert.UserConverter;
import com.sf.kh.model.Department;
import com.sf.kh.model.User;
import com.sf.kh.service.IBaseGoodsService;
import com.sf.kh.service.IDepartmentService;
import com.sf.kh.service.IGoodsStatisticsService;
import com.sf.kh.service.IOrganizationService;
import com.sf.kh.service.IUserService;
import com.sf.kh.util.Constants;
import com.sf.kh.util.WebContextHolder;

/***
 * 物资统计
 * @author 866316
 *
 */
@RestController
@RequestMapping(path = "statistics")
public class GoodsStatisticsController {
	
	private static final Logger logger = LoggerFactory.getLogger(GoodsStatisticsController.class);
	
	private @Resource IGoodsStatisticsService goodsStatisticsService;
	
	private @Resource IDepartmentService departmentService;
	
	private @Resource IOrganizationService organizationService;
	
	private @Resource IBaseGoodsService baseGoodsService;
	
	private @Resource IUserService userService;
	
	private @Resource IDepartmentService deptService;
	
	@Value("${sharePath}")
	private String sharePath;
	
	/***
	 * 概览
	 * @param params
	 * @return
	 */
	@GetMapping(path = "overview")
    public Result<OverviewVersionDataDto> getOverview(HttpSession session,@RequestParam(value = "custNo", required = true) String custNo) {
		User user = WebContextHolder.currentUser();
		Department dept = this.getUsersDept(user).getDept();
        user.setRoles(userService.queryUserRoles(user.getId()).getData());
        UserDto userDto = new UserConverter().convert(user);
		List<String> custNoList = this.getCustNos(custNo,userDto,dept);
		if(custNoList.size()==0){//参数传递错误
			return Result.failure(ResultCode.BAD_REQUEST, "您无权查看该页面数据!");
		}
		List<Department> deptList = getDepartmentListByCustNos(custNoList);
		return goodsStatisticsService.getOverviewVersionData(custNo, dept, deptList);
    }
	
	/**
	 * 更新用户的部门
	 * @param user
	 * @return
	 */
	private User getUsersDept(User user){
		user.setDept(departmentService.getDeptWithHierarchicalOrgById(user.getDeptId()));
		return user;
	}
	
	/***
	 * 获取所对应的部门
	 * @param custNos
	 * @return
	 */
	private List<Department> getDepartmentListByCustNos(List<String> custNos){
		return departmentService.getDeptsByCustNoList(custNos);
	}
	
	/***
	 * 获取用户所有的月结,
	 * 如果月结不符合则长度为0，表示非法传递
	 * @param custNo
	 * @param user
	 * @param dept
	 * @return
	 */
	private List<String> getCustNos(String custNo,UserDto userDto,Department dept){
		List<String> custNoList = new ArrayList<String>();
		if("MANAGER".equals(userDto.getRoleCode())){//超级管理员，无权查看该功能
        	return custNoList;
        }else{
        	if("全部".equals(custNo)){//添加自己
        		custNoList.add(dept.getCustNo());//先添加自己的
        		if(2 == userDto.getDeptPurview()){//管理员,添加孩子月结
        			List<Department> list = SpringContextHolder.getBean(IDepartmentService.class)
    	                    .getAllChildDepts(dept.getId());
    				if (CollectionUtils.isNotEmpty(list)) {
    					for (Department d : list) {
    						if (StringUtils.isNotBlank(d.getCustNo())) {
    							custNoList.add(d.getCustNo());
    						}
    					}
    				}
            	}
        	}else{//具体月结号
        		if(custNo.equals(dept.getCustNo())){//等于自己
    				custNoList.add(dept.getCustNo());//先添加自己的
    			}else{//检查是不是孩子月结
    				if(2 == userDto.getDeptPurview()){//管理员,添加孩子月结
    					List<Department> list = SpringContextHolder.getBean(IDepartmentService.class)
    		                    .getAllChildDepts(dept.getId());
    					if (CollectionUtils.isNotEmpty(list)) {
    						for (Department d : list) {
    							if (StringUtils.isNotBlank(d.getCustNo())&&d.getCustNo().equals(custNo)) {
    								custNoList.add(d.getCustNo());
    								break;
    							}
    						}
    					}
    				}
    			}
        	}
        }
        return custNoList;
	}
	
	/***
	 * 物资统计-单位方向
	 * @param params
	 * @return
	 */
	@GetMapping(path = "depts")
    public Result<Map<String,List<DepartmentStatisticsDto>>> getDepts(HttpSession session,@RequestParam(value = "custNo", required = true) String custNo) {
		User user = WebContextHolder.currentUser();
		Department dept = this.getUsersDept(user).getDept();
        user.setRoles(userService.queryUserRoles(user.getId()).getData());
        UserDto userDto = new UserConverter().convert(user);
		List<String> custNoList = this.getCustNos(custNo,userDto,dept);//获取到用户的月结列表
		if(custNoList.size()==0){//参数传递错误
			return Result.failure(ResultCode.BAD_REQUEST, "您无权查看该页面数据!");
		}
		//获取部门集合数据
		List<Department> deptList = getDepartmentListByCustNos(custNoList);
		return goodsStatisticsService.getDepartmentStatisticsData(deptList);
    }
	
	/***
	 * 物资统计-类别
	 * @param params
	 * @return
	 */
	@GetMapping(path = "goods")
    public Result<List<CategoryStatisticsDto>> getGoodses(HttpSession session,@RequestParam(value = "custNo", required = true) String custNo) {
		User user = WebContextHolder.currentUser();
		Department dept = this.getUsersDept(user).getDept();
        user.setRoles(userService.queryUserRoles(user.getId()).getData());
        UserDto userDto = new UserConverter().convert(user);
		List<String> custNoList = this.getCustNos(custNo,userDto,dept);//获取到用户的月结列表
		if(custNoList.size()==0){//参数传递错误
			return Result.failure(ResultCode.BAD_REQUEST, "您无权查看该页面数据!");
		}
		//获取部门集合数据
		List<Department> deptList = getDepartmentListByCustNos(custNoList);
		return goodsStatisticsService.getCategoryStatisticsData(deptList);
    }
	
	/***
	 * 物资统计-时间
	 * @param params
	 * @return
	 */
	@GetMapping(path = "time")
    public Result<List<TimeStatisticsDto>> getTime(HttpSession session,@RequestParam(value = "custNo", required = true) String custNo) {
		User user = WebContextHolder.currentUser();
		Department dept = this.getUsersDept(user).getDept();
        user.setRoles(userService.queryUserRoles(user.getId()).getData());
        UserDto userDto = new UserConverter().convert(user);
		List<String> custNoList = this.getCustNos(custNo,userDto,dept);//获取到用户的月结列表
		if(custNoList.size()==0){//参数传递错误
			return Result.failure(ResultCode.BAD_REQUEST, "您无权查看该页面数据!");
		}
		//获取部门集合数据
		List<Department> deptList = getDepartmentListByCustNos(custNoList);
		return goodsStatisticsService.getTimeStatisticsVersionData(deptList);
    }
	
	
	/***
	 * 物资统计-发运详情明细[我的]
	 * @param params
	 * @return
	 */
	@GetMapping(path = "detailBySend")
    public Result<Page<GoodsStatisticsDetailDto>> detailBySend(HttpSession session,PageRequestParams params) {
		String custNo = (String) params.getParams().get("custNo");
		User user = WebContextHolder.currentUser();
		Department dept = this.getUsersDept(user).getDept();
        user.setRoles(userService.queryUserRoles(user.getId()).getData());
        UserDto userDto = new UserConverter().convert(user);
		List<String> custNoList = this.getCustNos("全部",userDto,dept);//获取到用户的月结列表
		if(custNoList.size()==0){//参数传递错误
			return Result.failure(ResultCode.BAD_REQUEST, "您无权查看该页面数据!");
		}
		List<Department> deptList = getDepartmentListByCustNos(custNoList);//根据月结获取部门
		return goodsStatisticsService.query4pageBySend(this.getParams(params, deptList,"1",userDto));
    }
	
	private Map<String,Object> getParams(PageRequestParams params,List<Department> deptList,String type,UserDto userDto){
		Map<String,Object> paramsMap = params.getParams();
		String sendStartDate = (String)paramsMap.get("sendStartDate");
		String sendEndDate = (String)paramsMap.get("sendEndDate");
		if(null == sendStartDate || null == sendEndDate){
			Calendar startCalendar = Calendar.getInstance();  
			startCalendar.set(Calendar.DATE,startCalendar.get(Calendar.DATE)-8);  
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
			paramsMap.put("sendStartDate", format.format(startCalendar.getTime()));
			Calendar endCalendar = Calendar.getInstance();  
			endCalendar.set(Calendar.DATE,endCalendar.get(Calendar.DATE)-8);  
			paramsMap.put("sendEndDate", format.format(endCalendar.getTime()));
		}
		Long sendOrgTypeId = Long.parseLong(this.paramsTrasfer(paramsMap.get("sendOrgTypeId")));
		Long receiveOrgTypeId = Long.parseLong(this.paramsTrasfer(paramsMap.get("receiveOrgTypeId")));
		Long sendOrgId = Long.parseLong(this.paramsTrasfer(paramsMap.get("sendOrgId")));
		Long sendDeptId = Long.parseLong(this.paramsTrasfer(paramsMap.get("sendDeptId")));
		Long receiveOrgId = Long.parseLong(this.paramsTrasfer(paramsMap.get("receiveOrgId")));
		Long receiveDeptId = Long.parseLong(this.paramsTrasfer(paramsMap.get("receiveDeptId")));
		List<Long> deptNos = new ArrayList<Long>();
		for(Department d : deptList){
			deptNos.add(d.getId());
		}
		if("1".equals(type)){//发件明细
			if(-1 == sendOrgTypeId && -1 == sendOrgId && -1 == sendDeptId ){//没有选择任何值
				paramsMap.put("sendDeptIds", deptNos);
			}else{
				paramsMap.put("sendDeptIds",this.getDeptsByOrgIdAndDeptId(deptNos,sendOrgTypeId,sendOrgId, sendDeptId));
			}
			paramsMap.put("receiveDeptIds", this.getDeptsByOrgIdAndDeptId(null,receiveOrgTypeId,receiveOrgId, receiveDeptId));
		}else{//收件明细
			paramsMap.put("sendDeptIds", this.getDeptsByOrgIdAndDeptId(null,sendOrgTypeId,sendOrgId, sendDeptId));
			if(-1 == receiveOrgTypeId && -1 == receiveOrgId && -1 == receiveDeptId ){//没有选择任何值
				paramsMap.put("receiveDeptIds", deptNos);
			}else{
				paramsMap.put("receiveDeptIds",this.getDeptsByOrgIdAndDeptId(deptNos,receiveOrgTypeId,receiveOrgId, receiveDeptId));
			}
		}
		Long firstCategoryId = Long.parseLong(this.paramsTrasfer(paramsMap.get("firstCategoryId")));
		Long secondCategoryId = Long.parseLong(this.paramsTrasfer(paramsMap.get("secondCategoryId")));
		Long goodsId = Long.parseLong(this.paramsTrasfer(paramsMap.get("goodsId")));
		List<Long> goodsNos = new ArrayList<Long>();
		if(-1 != firstCategoryId){
			if(-1 == secondCategoryId){//根据类别一获取所有的物资
				goodsNos.addAll(baseGoodsService.getListByCategoryIds(firstCategoryId, new ArrayList<Long>()));
			}else{
				if(-1 == goodsId){
					List<Long> categoryArr = new ArrayList<Long>();
					categoryArr.add(secondCategoryId);
					goodsNos.addAll(baseGoodsService.getListByCategoryIds(firstCategoryId, categoryArr));
				}else{
					goodsNos.add(goodsId);
				}
			}
		}
		paramsMap.put("goodsIds", goodsNos);
		logger.info("物资统计-物资发收明细请求参数转换结果：{}",paramsMap.toString());
		return paramsMap;
	}
	
	private String paramsTrasfer(Object obj){
		if(null == obj){
			return "-1";
		}else{
			return (String)obj;
		}
	}
	
	/***
	 * 根据组织或部门id获取部门编号
	 */
	private List<Long> getDeptsByOrgIdAndDeptId(List<Long> validDeptNos,Long orgTypeId,Long orgId,Long deptId){
		List<Long> deptNos = new ArrayList<Long>();
		if(-1 != deptId){
			if(null != validDeptNos){
				if(validDeptNos.contains(deptId)){//校验有效性
					deptNos.add(deptId);
				}
			}else{
				deptNos.add(deptId);
			}
		}else if(-1 != orgId){
			List<Long> deptNosByOrgId =  this.getDeptsByOrgId(orgId);
			if(null != validDeptNos){
				for(Long id : deptNosByOrgId){
					for(Long fact : validDeptNos){//校验有效性
						if(id.equals(fact)){
							deptNos.add(id);
						}
					}
				}
			}else{
				deptNos.addAll(deptNosByOrgId);
			}
		}else if(-1 != orgTypeId){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("orgTypeId", orgTypeId);
			List<Department> deptList = departmentService.getDeptsWithHierarchicalOrgByOrgTypeId(params);
			if(null != validDeptNos){
				for(Department dt : deptList){
					for(Long fact : validDeptNos){//校验有效性
						if(dt.getId().equals(fact)){
							deptNos.add(dt.getId());
						}
					}
				}
			}else{
				for(Department dt : deptList){
					deptNos.add(dt.getId());
				}
			}
		}
		return deptNos;
	}
	
	/***
	 * 获取组织下面所有的部门编号
	 */
	private List<Long> getDeptsByOrgId(Long orgId){
		List<Department> deptList = departmentService.getAllDeptsByOrgId(orgId);
		List<Long> deptNos = new ArrayList<Long>();
		if(null == deptList || deptList.size()==0){
			deptNos.add(-1L);
		}else{
			for(Department d : deptList){
				deptNos.add(d.getId());
			}
		}
		return deptNos;
	}
	
	/***
	 * 物资统计-发运详情导出
	 * @param params
	 * @return
	 */
	@GetMapping(path = "exportBySend")
    public void exportBySend(HttpSession session,HttpServletResponse response,PageRequestParams params) {
		String fileFolderPath;
		if (StringUtils.isBlank(sharePath)) {
			fileFolderPath = session.getServletContext().getRealPath("/uploadFile/");
		} else {
			fileFolderPath = sharePath.trim();
		}
		fileFolderPath = "export"+File.separator;
		User user = WebContextHolder.currentUser();
		Department dept = this.getUsersDept(user).getDept();//当前用户的部门
		String custNo = (String) params.getParams().get("custNo");
        user.setRoles(userService.queryUserRoles(user.getId()).getData());
        UserDto userDto = new UserConverter().convert(user);
		List<String> custNoList = this.getCustNos("全部",userDto,dept);//获取到用户的月结列表
		if(custNoList.size()==0){//参数传递错误
			return ;
		}
		List<Department> deptList = getDepartmentListByCustNos(custNoList);
		File file = goodsStatisticsService.exportsGoodsList(fileFolderPath, "1", this.getParams(params,
				deptList, "1",userDto));
		String templeFileName = "发运物资明细.csv";
		try {
			Constants.exportFile(response, templeFileName, file);
			logger.info("发运物资明细导出成功");
		} catch (IOException e) {
			logger.info("发运物资明细导出异常原因：{}",e.getMessage());
		}
    }
	
	/***
	 * 物资统计-接收详情明细
	 * @param params
	 * @return
	 */
	@GetMapping(path = "detailByReceive")
    public Result<Page<GoodsStatisticsDetailDto>> detailByReceive(HttpSession session,PageRequestParams params) {
		User user = WebContextHolder.currentUser();
		Department dept = this.getUsersDept(user).getDept();//当前用户的部门
		String custNo = (String) params.getParams().get("custNo");
        user.setRoles(userService.queryUserRoles(user.getId()).getData());
        UserDto userDto = new UserConverter().convert(user);
		List<String> custNoList = this.getCustNos("全部",userDto,dept);//获取到用户的月结列表
		if(custNoList.size()==0){//参数传递错误
			return Result.failure(ResultCode.BAD_REQUEST, "您无权查看该页面数据!");
		}
		List<Department> deptList = getDepartmentListByCustNos(custNoList);
		return goodsStatisticsService.query4pageByReceive(this.getParams(params,deptList, "2",
				userDto));
    }
	
	/***
	 * 物资统计-接收详情导出
	 * @param params
	 * @return
	 */
	@GetMapping(path = "exportByReceive")
    public void exportByReceive(HttpSession session,HttpServletResponse response,PageRequestParams params) {
		String fileFolderPath;
		if (StringUtils.isBlank(sharePath)) {
			fileFolderPath = session.getServletContext().getRealPath("/uploadFile/");
		} else {
			fileFolderPath = sharePath.trim();
		}
		fileFolderPath = "export"+File.separator;
		User user = WebContextHolder.currentUser();
		Department dept = this.getUsersDept(user).getDept();
        user.setRoles(userService.queryUserRoles(user.getId()).getData());
        UserDto userDto = new UserConverter().convert(user);
        String custNo = (String) params.getParams().get("custNo");
		List<String> custNoList = this.getCustNos("全部",userDto,dept);//获取到用户的月结列表
		if(custNoList.size()==0){//参数传递错误
			return ;
		}
		List<Department> deptList = getDepartmentListByCustNos(custNoList);
		File file = goodsStatisticsService.exportsGoodsList(fileFolderPath, "2", this.getParams(params, 
				deptList,"2",userDto));
		String templeFileName = "接收物资明细.csv";
		try {
			Constants.exportFile(response, templeFileName, file);
			logger.info("接收物资明细导出成功");
		} catch (IOException e) {
			logger.info("接收物资明细导出异常原因：{}",e.getMessage());
		}
		
    }
	
}
