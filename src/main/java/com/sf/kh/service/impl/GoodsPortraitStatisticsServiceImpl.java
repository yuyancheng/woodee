package com.sf.kh.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import code.ponfee.commons.model.Result;
import com.sf.kh.dao.IGoodsPortraitStatisticsDao;
import com.sf.kh.dto.DepartmentStatisticsDto;
import com.sf.kh.dto.PortraitStatisticsDto;
import com.sf.kh.model.Department;
import com.sf.kh.model.PortraitStatisticsDepartment;
import com.sf.kh.model.PortraitStatisticsMonth;
import com.sf.kh.model.PortraitStatisticsName;
import com.sf.kh.model.PortraitStatisticsRate;
import com.sf.kh.service.IDepartmentService;
import com.sf.kh.service.IGoodsPortraitStatisticsService;
/***
 * 物资纵向统计
 * @author 866316
 *
 */
@Service
public class GoodsPortraitStatisticsServiceImpl implements IGoodsPortraitStatisticsService{
	
	private static final Logger logger = LoggerFactory.getLogger(GoodsPortraitStatisticsServiceImpl.class);
	
	private @Resource IDepartmentService departmentService;
	
	private @Resource IGoodsPortraitStatisticsDao goodsPortraitStatisticsDao;

	@Override
	public Result<Map<String,List<DepartmentStatisticsDto>>> getDepartmentStatisticsData(
			PortraitStatisticsDto psd) {
		//1，获取方向（单位）数据
		List<PortraitStatisticsDepartment> deptList = goodsPortraitStatisticsDao.getDeptList(psd);
		
		//2,获取发运类别/物资占比
		List<PortraitStatisticsRate> rateList = goodsPortraitStatisticsDao.getRateList(psd);
		
		//加工收运物资数据统计
		Map<String, List<DepartmentStatisticsDto>> resultMap= new HashMap<String,List<DepartmentStatisticsDto>>();
		resultMap.put("dept", this.processDeptData(deptList));
		resultMap.put("rate", this.processRateData(rateList));
		return Result.success(resultMap);
	}
	/***
	 * 默认已经是根据比率倒叙排序
	 * 比率统计前五+其他
	 * @param list
	 * @return
	 */
	private List<DepartmentStatisticsDto> processRateData(List<PortraitStatisticsRate> list){
		List<DepartmentStatisticsDto> deptList = new ArrayList<DepartmentStatisticsDto>();
		DepartmentStatisticsDto other = new DepartmentStatisticsDto();
		other.setDeptName("其他");
		for(int i =0;i<list.size();i++){
			PortraitStatisticsRate psr = list.get(i);
			if(i<5){//前五
				DepartmentStatisticsDto dsd = new DepartmentStatisticsDto();
				dsd.setDeptName(psr.getRateName());
				dsd.setDeptRate(psr.getRate().doubleValue());
				deptList.add(dsd);
			}else{
				other.setDeptRate(other.getDeptRate()+psr.getRate().doubleValue());
			}
		}
		if(list.size()>5){
			deptList.add(other);
		}
		return deptList;
	}
	
	private List<DepartmentStatisticsDto> processDeptData(List<PortraitStatisticsDepartment> list){
		Map<String,DepartmentStatisticsDto> deptMap = new HashMap<String,DepartmentStatisticsDto>();
		//加工发运物资数据统计
		for(PortraitStatisticsDepartment psd : list){
			//根据部门id，获取顶级部门id
			Department tempDept = departmentService.getDeptWithHierarchicalOrgById(Long.valueOf(psd.getReceiveCompanyId()));
			if(null == tempDept){
				continue;
			}
			String statisticName = tempDept.getBiRelName();
			if(null == statisticName){
				continue;
			}
			DepartmentStatisticsDto dsd = deptMap.get(statisticName);
			if(null == dsd){
				dsd = new DepartmentStatisticsDto();
				dsd.setDeptName(statisticName);
			}
			dsd.setDeptAmount(dsd.getDeptAmount()+psd.getGoodsNum().intValue());
			deptMap.put(statisticName, dsd);
		}
		double count = 0;//总量
		for (Map.Entry<String, DepartmentStatisticsDto> entry : deptMap.entrySet()) { 
			count += entry.getValue().getDeptAmount();
		}
		if(0 == count){//没有统计数据
			return new ArrayList<DepartmentStatisticsDto>();
		}
		List<DepartmentStatisticsDto> result = new ArrayList<DepartmentStatisticsDto>();
		for (Map.Entry<String, DepartmentStatisticsDto> entry : deptMap.entrySet()) { 
			DepartmentStatisticsDto dto = entry.getValue();
			BigDecimal bg = new BigDecimal(dto.getDeptAmount()/count);
			double temp = bg.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();//率
			dto.setDeptRate(temp);
			result.add(dto);
		}
		return result;
	}

	@Override
	public Result<List<PortraitStatisticsName>> getPortraitStatisticsNameData(PortraitStatisticsDto psd) {
		return Result.success(goodsPortraitStatisticsDao.getNameList(psd));
	}
	
	@Override
	public Result<List<PortraitStatisticsMonth>> getPortraitStatisticsMonthData(PortraitStatisticsDto psd) {
		return Result.success(goodsPortraitStatisticsDao.getMonthList(psd));
	}
	
}
