package com.sf.kh.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.sf.kh.dto.UserDto;
import com.sf.kh.dto.convert.UserConverter;
import com.sf.kh.exception.BusinessException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageRequestParams;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import com.sf.kh.model.DespatchBatch;
import com.sf.kh.model.DespatchGoods;
import com.sf.kh.model.User;
import com.sf.kh.service.IDepartmentService;
import com.sf.kh.service.IDespatchBatchService;
import com.sf.kh.service.IUserService;
import com.sf.kh.util.Constants;
import com.sf.kh.util.DateUtil;
import com.sf.kh.util.WebContextHolder;
/***
 * 发运批次
 * @author 866316
 *
 */
@RestController
@RequestMapping(path = "despatchBatch")
public class DespatchBatchController {
	private @Resource IDespatchBatchService despatchBatchService;
	
	private @Resource IUserService userService;
	
	private @Resource IDepartmentService deptService;
	
	/***
	 * 新增批次发运记录
	 */
	@PostMapping(path = "save")
    public Result<String> saveDespatchBatch(HttpSession session,@RequestBody DespatchBatch despatchBatch) {
		String userId = String.valueOf(WebContextHolder.currentUser().getId());
		if(null  == despatchBatch){
			return Result.failure(ResultCode.BAD_REQUEST, "请提供发运批次信息!");
		}
		//最后真实的id号
		long serverId = despatchBatchService.getTodayMaxBatchCode();
		DespatchBatch batchTempObj = new DespatchBatch();
		if(-1 == serverId){//今天还没有建单号
			SimpleDateFormat sf = new SimpleDateFormat("yyMMddHH");
		    Calendar c = Calendar.getInstance();
		    String dateStr = sf.format(c.getTime())+"0001";
		    batchTempObj.setBatchId(Long.valueOf(dateStr));
		}else{//有单号，则直接在此基础上累加1
			batchTempObj.setBatchId(serverId+1);
		}
		
		String dateStr = DateUtil.formatDateYmDhMs(new Date());
		batchTempObj.setCreateBy(userId);
		batchTempObj.setCreateTm(dateStr);
		batchTempObj = despatchBatchService.insertBatchId(batchTempObj);
		if(null == batchTempObj){
			return Result.failure(ResultCode.SERVER_ERROR, "创建失败，请重试!");
		}
		//1.添加物资记录
		List<DespatchGoods> goodsList = despatchBatch.getDespatchGoodsList();
		
		Integer goodsCount = 0;//物资统计
		String packageType = Constants.PACKAGE_SINGLE;
		if(goodsList == null){
			return Result.failure(ResultCode.BAD_REQUEST, "请输入要发运的物资信息!");
		}
		long goodsId = goodsList.get(0).getGoodsId();
		
		for(DespatchGoods goods: goodsList){
			goodsCount += goods.getGoodsNum();
			goods.setBatchId(batchTempObj.getBatchId());
			goods.setCreateBy(userId);
			goods.setCreateTm(dateStr);
			if(goodsId!= goods.getGoodsId()){//存在多个
				packageType = Constants.PACKAGE_MIX;//混合类型
			}
		}
		if(0 == goodsCount){
			return Result.failure(ResultCode.BAD_REQUEST, "请输入要发运的物资信息!");
		}
		despatchBatch.setCreateBy(userId);
		despatchBatch.setCreateTm(dateStr);
		despatchBatch.setBatchId(batchTempObj.getBatchId());
		despatchBatch.setPackageType(Integer.valueOf(packageType));
		despatchBatch.setGoodsNum(goodsCount);
		String remark = despatchBatch.getRemark();
		if(StringUtils.isNotBlank(remark)){//有值
			if(remark.length()>100){
				remark = remark.substring(0, 100);//截取中文字符串
				despatchBatch.setRemark(remark);
			}
		}
		return despatchBatchService.updateByBatchId(despatchBatch);
    }
	
	/***
	 * 获取待发运的批次记录
	 * @param params
	 * @return
	 */
	@GetMapping(path = "list")
    public Result<Page<DespatchBatch>> list(HttpSession session,PageRequestParams params) {
		User user = WebContextHolder.currentUser();
		user = this.getUsersDept(user);
        user.setRoles(userService.queryUserRoles(user.getId()).getData());
        UserDto userDto = new UserConverter().convert(user);
        if("MANAGER".equals(userDto.getRoleCode())){
        	params.put("deptId", null);
        }else{
        	params.put("deptId", user.getDeptId());
        }
        return despatchBatchService.query4page(params.getParams());
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
	
	/****
	 * 获取待发运批次的指定记录并打印
	 */
	@PostMapping(path = "print")
    public Result<DespatchBatch> print(HttpSession session,@RequestParam(value = "batchId", required = true) String batchId) {
		User user = WebContextHolder.currentUser();
		user = this.getUsersDept(user);
        user.setRoles(userService.queryUserRoles(user.getId()).getData());
        UserDto userDto = new UserConverter().convert(user);
        Long deptId = null;
        if(!"MANAGER".equals(userDto.getRoleCode())){
        	deptId = user.getDeptId();
        }
    	return despatchBatchService.print(batchId,deptId);
    }
	
	/****
	 * 根据批次号，获取物资集合
	 */
	@PostMapping(path = "getGoodsList")
    public Result<List<DespatchGoods>> getGoodsList(HttpSession session,@RequestParam(value = "batchId", required = true) String batchId) {
		Long userId = WebContextHolder.currentUser().getId();
    	return despatchBatchService.getGoodsList(batchId,userId);
    }

	/****
	 * 更新批次号的打印时间
	 */
	@PostMapping(path = "updatePrintTime")
    public Result<Boolean> updatePrintTime(HttpSession session,@RequestParam(value = "batchId", required = true) String batchId) {
		Long userId = WebContextHolder.currentUser().getId();
    	return despatchBatchService.updatePrintTime(batchId, userId);
    }

	@PostMapping(path = "goodsList")
	public Result<List<DespatchGoods>> goodsList(HttpSession session, @RequestBody Map<String, String> params) {
		String batchId = (String) params.get("batchId");

		if(StringUtils.isBlank(batchId)){
			throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "batchId 不能为空");
		}
		return despatchBatchService.getGoodsListWithBase(batchId);
	}

}
