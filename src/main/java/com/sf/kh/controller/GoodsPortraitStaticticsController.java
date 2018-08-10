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
import com.sf.kh.dto.DepartmentStatisticsDto;
import com.sf.kh.dto.GoodsStatisticsDetailDto;
import com.sf.kh.dto.PortraitStatisticsDto;
import com.sf.kh.dto.UserDto;
import com.sf.kh.dto.convert.UserConverter;
import com.sf.kh.model.Department;
import com.sf.kh.model.GoodsDept;
import com.sf.kh.model.Organization;
import com.sf.kh.model.PortraitStatisticsMonth;
import com.sf.kh.model.PortraitStatisticsName;
import com.sf.kh.model.User;
import com.sf.kh.service.IBaseGoodsService;
import com.sf.kh.service.IDepartmentService;
import com.sf.kh.service.IGoodsPortraitStatisticsService;
import com.sf.kh.service.IGoodsStatisticsService;
import com.sf.kh.service.IUserService;
import com.sf.kh.util.Constants;
import com.sf.kh.util.WebContextHolder;

/***
 * 物资纵向统计
 * @author 866316
 *
 */
@RestController
@RequestMapping(path = "portraitStatistics")
public class GoodsPortraitStaticticsController {
	
	private static final Logger logger = LoggerFactory.getLogger(GoodsPortraitStaticticsController.class);
	
	private @Resource IGoodsPortraitStatisticsService goodsPortraitStatisticsService;
	
	private @Resource IBaseGoodsService baseGoodsService;
	
	private @Resource IUserService userService;
	
	private @Resource IDepartmentService deptService;
	
	private @Resource IGoodsStatisticsService goodsStatisticsService;
	
	@Value("${sharePath}")
	private String sharePath;
	
	
	/***
	 * 物资纵向统计-单位方向
	 * @param params
	 * @return
	 */
	@GetMapping(path = "depts")
    public Result<Map<String,List<DepartmentStatisticsDto>>> getDepts(HttpSession session,@RequestParam(value = "specialName", required = false) String specialName) {
		User user = WebContextHolder.currentUser();
		PortraitStatisticsDto psd = this.getParams(user,specialName);
		if(null == psd){
			return Result.failure(ResultCode.BAD_REQUEST, "当前登录用户没有权限访问该页面");
		}
		return goodsPortraitStatisticsService.getDepartmentStatisticsData(psd);
    }
	
	/***
	 * 物资纵向统计-时间
	 * @param params
	 * @return
	 */
	@GetMapping(path = "time")
    public Result<List<PortraitStatisticsMonth>> getTime(HttpSession session,@RequestParam(value = "specialName", required = true) String specialName) {
		User user = WebContextHolder.currentUser();
		PortraitStatisticsDto psd = this.getParams(user,specialName);
		if(null == psd){
			return Result.failure(ResultCode.BAD_REQUEST, "当前登录用户没有权限访问该页面");
		}
		return goodsPortraitStatisticsService.getPortraitStatisticsMonthData(psd);
    }
	
	/***
	 * 物资统计-类别
	 * @param params
	 * @return
	 */
	@GetMapping(path = "goods")
    public Result<List<PortraitStatisticsName>> getGoodses(HttpSession session,@RequestParam(value = "specialName", required = true) String specialName) {
		User user = WebContextHolder.currentUser();
		PortraitStatisticsDto psd = this.getParams(user,specialName);
		if(null == psd){
			return Result.failure(ResultCode.BAD_REQUEST, "当前登录用户没有权限访问该页面");
		}
		return goodsPortraitStatisticsService.getPortraitStatisticsNameData(psd);
    }
	
	
	
	/**
	 * 构造参数
	 * @param user
	 * @return
	 */
	private PortraitStatisticsDto getParams(User user,String specialName){
        if (user.getDeptId() != null) {
            user.setDept(deptService.getDeptWithHierarchicalOrgById(user.getDeptId()));
        }
        user.setRoles(userService.queryUserRoles(user.getId()).getData());
        UserDto userDto = new UserConverter().convert(user);
        if("MANAGER".equals(userDto.getRoleCode())){
        	PortraitStatisticsDto psd = new PortraitStatisticsDto();
			if("全部".equals(specialName)){
				psd.setType(3);
				psd.setTypeName("admin");
			}else{//切换到具体专业名字
				psd.setType(2);
				GoodsDept gd = new GoodsDept();
				gd.setParentCategoryId(Long.valueOf(specialName));
				List<GoodsDept> gdList = baseGoodsService.getGoodsDept(gd);
				if(null != gdList && gdList.size()>0){
					psd.setTypeName(String.valueOf(gdList.get(0).getParentOrgId()));
				}else{
					psd.setTypeName(String.valueOf("-1"));
				}
			}
			return psd;
        }else{
        	if (null != userDto.getOrgMaterialMark() 
        			&& 1 == userDto.getOrgMaterialMark()//物资相关用户
        			&& 2 == userDto.getDeptPurview()){//只有专业局和业务处的管理员才能查看该功能
            	PortraitStatisticsDto psd = new PortraitStatisticsDto();
    			Department dept = deptService.getDeptWithHierarchicalOrgById(userDto.getDeptId());
    			if(null == dept.getOrganization().getParentOrganization()){//专业局管理员
    				psd.setType(2);
    			}else{//业务处管理员
    				psd.setType(1);
    			}
    			psd.setTypeName(String.valueOf(dept.getOrganization().getId()));
    			return psd;
    		}
        }
		return null;
	}
	
	/***
	 * 物资统计-发运详情明细
	 * @param params
	 * @return
	 */
	@GetMapping(path = "detailBySend")
    public Result<Page<GoodsStatisticsDetailDto>> detailBySend(HttpSession session,PageRequestParams params) {
		User user = WebContextHolder.currentUser();
		Department dept = this.getUsersDept(user).getDept();
        user.setRoles(userService.queryUserRoles(user.getId()).getData());
        UserDto userDto = new UserConverter().convert(user);
        Map<String,Object> paramsMap = this.getDetailParams(params,"1",userDto,dept);
        if(null == paramsMap){
        	return Result.failure(ResultCode.BAD_REQUEST, "您提供的参数有误!");
        }
		return goodsStatisticsService.query4pageBySend(paramsMap);
    }
	
	private Map<String,Object> getDetailParams(PageRequestParams params,String type,UserDto userDto,Department userDept){
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
		paramsMap.put("sendDeptIds", this.getDeptsByOrgIdAndDeptId(sendOrgTypeId,sendOrgId, sendDeptId));
		paramsMap.put("receiveDeptIds", this.getDeptsByOrgIdAndDeptId(receiveOrgTypeId,receiveOrgId, receiveDeptId));
		
		Long firstCategoryId = Long.parseLong(this.paramsTrasfer(paramsMap.get("firstCategoryId")));
		Long secondCategoryId = Long.parseLong(this.paramsTrasfer(paramsMap.get("secondCategoryId")));
		Long goodsId = Long.parseLong(this.paramsTrasfer(paramsMap.get("goodsId")));
		boolean isZyjManager = false;
		List<Long> categoryIdArr = new ArrayList<Long>();
		List<Long> goodsNos = new ArrayList<Long>();
		//超级管理员不限，只有专业局和业务处的管理员才能够看
		if(!"MANAGER".equals(userDto.getRoleCode())){
			if (null != userDto.getOrgMaterialMark() 
        			&& 1 == userDto.getOrgMaterialMark()//物资相关用户
        			&& 2 == userDto.getDeptPurview()){//只有专业局和业务处的管理员才能查看该功能
				if(null == userDept.getOrganization().getParentOrganization()){//专业局管理员
					if(-1 != firstCategoryId){
						GoodsDept gd = new GoodsDept();
						gd.setParentOrgId(userDept.getOrganization().getId());
						List<GoodsDept> gdList = baseGoodsService.getGoodsDept(gd);
						for(GoodsDept gdTemp : gdList){
							if(gdTemp.getParentCategoryId().equals(firstCategoryId)){//参数正确
								isZyjManager = true;
								break;
							}
						}
					}
					if(false == isZyjManager){//是专业局管理员，但是查询参数传递错误
						logger.error("专业局管理员没有获取到专业局组织所对应的专业，或者该专业局未配置专业");
						goodsNos.add(-2L);
					}else{//匹配成功
						if(-1 == goodsId){
							if(-1 != secondCategoryId){
								categoryIdArr.add(secondCategoryId);
							}
							goodsNos.addAll(baseGoodsService.getListByCategoryIds(firstCategoryId, categoryIdArr));
						}else{
							goodsNos.add(goodsId);
						}
					}
				}else{//业务处管理员
					Organization ownOrg = userDept.getOrganization();
					Organization parentOrg = ownOrg.getParentOrganization();
					GoodsDept gd = new GoodsDept();
					gd.setParentOrgId(parentOrg.getId());
					gd.setOrgId(ownOrg.getId());
					List<GoodsDept> gdList = baseGoodsService.getGoodsDept(gd);
					for(GoodsDept gdTemp : gdList){
						if(-1 == secondCategoryId){//没有给出具体，那么只看自己有的类别
							categoryIdArr.add(gdTemp.getCategoryId());
						}else{
							if(gdTemp.getParentCategoryId().equals(firstCategoryId)
									&&gdTemp.getCategoryId().equals(secondCategoryId)){//给出了具体类别
								categoryIdArr.add(gdTemp.getCategoryId());
							}
						}
						
					}
					if(categoryIdArr.size() == 0){//参数提供错误
						logger.error("业务处管理员没有获取到业务处组织所对应的类别，或者该业务处未配置类别");
						goodsNos.add(-2L);
					}else{
						if(-1 == goodsId){//专业类别随便切
							goodsNos.addAll(baseGoodsService.getListByCategoryIds(firstCategoryId, categoryIdArr));
						}else{
							goodsNos.add(goodsId);
						}
					}
				}
			}
		}else{//超级管理员，根据选择的条件查看物资
			if(-1 != goodsId){
				goodsNos.add(goodsId);
			}else{
				if(-1 != secondCategoryId){
					categoryIdArr.add(secondCategoryId);
					goodsNos.addAll(baseGoodsService.getListByCategoryIds(firstCategoryId, categoryIdArr));
				}else{
					if(-1 != firstCategoryId){
						GoodsDept gd = new GoodsDept();
						gd.setParentCategoryId(firstCategoryId);
						List<GoodsDept> gdList = baseGoodsService.getGoodsDept(gd);
						for(GoodsDept gdTemp : gdList){
							categoryIdArr.add(gdTemp.getCategoryId());
						}
						goodsNos.addAll(baseGoodsService.getListByCategoryIds(firstCategoryId, categoryIdArr));
					}
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
			String temp = obj.toString();
			if(StringUtils.isBlank(temp)){
				return "-1";
			}else{
				return temp;
			}
		}
	}
	
	/***
	 * 根据组织或部门id获取部门编号
	 */
	private List<Long> getDeptsByOrgIdAndDeptId(Long orgTypeId,Long orgId,Long deptId){
		List<Long> deptNos = new ArrayList<Long>();
		if(-1 != deptId){
			deptNos.add(deptId);
		}else if(-1 != orgId){
			deptNos.addAll(this.getDeptsByOrgId(orgId));
		}else if(-1 != orgTypeId){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("orgTypeId", orgTypeId);
			List<Department> deptList = deptService.getDeptsWithHierarchicalOrgByOrgTypeId(params);
			for(Department dt : deptList){
				deptNos.add(dt.getId());
			}
		}
		return deptNos;
	}
	
	/***
	 * 获取组织下面所有的部门编号
	 */
	private List<Long> getDeptsByOrgId(Long orgId){
		List<Department> deptList = deptService.getAllDeptsByOrgId(orgId);
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
        user.setRoles(userService.queryUserRoles(user.getId()).getData());
        UserDto userDto = new UserConverter().convert(user);
        Map<String,Object> paramsMap = this.getDetailParams(params,"1",userDto,dept);
        if(null == paramsMap){
        	return;
        }
		File file = goodsStatisticsService.exportsGoodsList(fileFolderPath, "1", paramsMap);
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
        user.setRoles(userService.queryUserRoles(user.getId()).getData());
        UserDto userDto = new UserConverter().convert(user);
        Map<String,Object> paramsMap = this.getDetailParams(params,"2",userDto,dept);
        if(null == paramsMap){
        	return Result.failure(ResultCode.BAD_REQUEST, "您提供的参数有误!");
        }
		return goodsStatisticsService.query4pageByReceive(paramsMap);
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
        Map<String,Object> paramsMap = this.getDetailParams(params,"2",userDto,dept);
        if(null == paramsMap){
        	return ;
        }
		File file = goodsStatisticsService.exportsGoodsList(fileFolderPath, "2", paramsMap);
		String templeFileName = "接收物资明细.csv";
		try {
			Constants.exportFile(response, templeFileName, file);
			logger.info("接收物资明细导出成功");
		} catch (IOException e) {
			logger.info("接收物资明细导出异常原因：{}",e.getMessage());
		}
		
    }
	
	/**
	 * 更新用户的部门
	 * @param user
	 * @return
	 */
	private User getUsersDept(User user){
		user.setDept(deptService.getDeptWithHierarchicalOrgById(user.getDeptId()));
		return user;
	}
}
