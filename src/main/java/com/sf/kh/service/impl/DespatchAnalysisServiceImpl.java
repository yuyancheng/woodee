package com.sf.kh.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sf.kh.dao.IIndexRankTop5VersionDao;
import com.sf.kh.dao.mapper.DespatchAnalysisMapper;
import com.sf.kh.dto.ArrivalAnalysisDto;
import com.sf.kh.dto.EffectivenessAnalysisDto;
import com.sf.kh.dto.ExceptionalAnalysisDto;
import com.sf.kh.dto.FlowDirectionAnalysisDto;
import com.sf.kh.model.IndexRankTop5Version;
import com.sf.kh.model.ScreenConfig;
import com.sf.kh.service.IDespatchAnalysisService;
import com.sf.kh.service.IScreenStatisticsService;

import code.ponfee.commons.model.Result;

/**
 * 配送分析、流向分析大屏幕
 * @author 835089
 *
 */
@Service
public class DespatchAnalysisServiceImpl implements IDespatchAnalysisService {
	
	private static Logger logger = LoggerFactory.getLogger(DespatchAnalysisServiceImpl.class);
	
	/**
	 * 获取大屏配置信息
	 */
	@Resource
	private IScreenStatisticsService screenStatisticsService;
	
	/** 
	 * 配送分析、流向分析
	 */
	@Resource
	private DespatchAnalysisMapper despatchAnalysisMapper;
	
	@Resource
	private IIndexRankTop5VersionDao indexRankTop5VersionDao;

	/* 
	 * 到货分析
	 * @see com.sf.kh.service.IDespatchAnalysisService#findArrivalAnalysisByCond(java.util.Map)
	 */
	@Override
	public List<ArrivalAnalysisDto> findArrivalAnalysisByCond(Map<String, Object> paramsMap) {
		
		Map<String, Object> params = buildParams(paramsMap);
		if(null == params) {
			return null;
		}
		List<ArrivalAnalysisDto> list = this.despatchAnalysisMapper.findArrivalAnalysisByCond(params);
		Long totalNum = 0L;
		Long receivedNum = 0L;
		for (ArrivalAnalysisDto t : list) {
			totalNum+=t.getGoodsNum();
			if(t.getWaybillStatus().trim().equals("已签收")) {
				receivedNum = t.getGoodsNum();
			}
		}
		list.add(new ArrivalAnalysisDto("顺丰发货总量",totalNum));
		Long zb = null;
		if(totalNum != 0) {
			zb =new BigDecimal(100.00*receivedNum/totalNum).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
		}
		list.add(new ArrivalAnalysisDto("占比",zb));
		return list;
	}

	/* 
	 * 时效分析
	 * @see com.sf.kh.service.IDespatchAnalysisService#findEffectivenessAnalysisByCond(java.util.Map)
	 */
	@Override
	public List<EffectivenessAnalysisDto> findEffectivenessAnalysisByCond(Map<String, Object> paramsMap) {
		
		Map<String, Object> params = buildParams(paramsMap);
		if(null == params) {
			return null;
		}
		List<EffectivenessAnalysisDto> list = this.despatchAnalysisMapper.findEffectivenessAnalysisByCond(params);
		
		List<EffectivenessAnalysisDto> result = new ArrayList<>();
		//总数
		Long allNum = 0L;
		for (EffectivenessAnalysisDto t : list) {
			allNum+=t.getTotalNum();
		}
		//算占比
		if(allNum != 0) {
			
			for (EffectivenessAnalysisDto t : list) {
				EffectivenessAnalysisDto dto = new EffectivenessAnalysisDto();
				dto.setStat(t.getStat());
				dto.setZb(new BigDecimal(100.00*t.getTotalNum()/allNum).setScale(2, BigDecimal.ROUND_HALF_UP)+"");
				result.add(dto);
			}
		}
		return result;
	}

	/* 
	 * 异常分析
	 * @see com.sf.kh.service.IDespatchAnalysisService#findExceptionalAnalysisByCond(java.util.Map)
	 */
	@Override
	public Map<String, Object> findExceptionalAnalysisByCond(Map<String, Object> paramsMap) {
		
		Map<String, Object> params = buildParams(paramsMap);
		if(null == params) {
			return null;
		}
		Map<String, Object> resultsMap = buildParams(paramsMap);
		List<ExceptionalAnalysisDto> exceptionList = this.despatchAnalysisMapper.findExceptionalAnalysisByCond(params);
		//超时未签收
		List<ExceptionalAnalysisDto> noSignList = new ArrayList<>();
		//超时无物流
		List<ExceptionalAnalysisDto> noRouteList = new ArrayList<>();
		//派送异常
		List<ExceptionalAnalysisDto> excList = new ArrayList<>();
		for (ExceptionalAnalysisDto t : exceptionList) {
			if("超时未签收".equals(t.getStat())) {
				noSignList.add(t);
			}
			if("超时无物流".equals(t.getStat())) {
				noRouteList.add(t);		
			}
			if("派送异常".equals(t.getStat())) {
				excList.add(t);
			}
		}
		resultsMap.put("noSignList", noSignList);
		resultsMap.put("noRouteList", noRouteList);
		resultsMap.put("excList", excList);
		return resultsMap;
	}

	@Override
	public List<FlowDirectionAnalysisDto> findFlowDirectionAnalysisBycond(Map<String, Object> paramsMap) {
		
		Map<String, Object> params = buildParams(paramsMap);
		if(null == params) {
			return null;
		}
		
		//发货总量
		Long totalNum = 0L;
		List<FlowDirectionAnalysisDto> result = new ArrayList<>();
		List<FlowDirectionAnalysisDto> list = this.despatchAnalysisMapper.findFlowDirectionAnalysisBycond(params);
		for (FlowDirectionAnalysisDto t : list) {
			totalNum+=t.getGoodsNum()==null?0L:t.getGoodsNum();
		}
		
		if(totalNum != 0) {
			for (FlowDirectionAnalysisDto t : list) {
				String zb = new BigDecimal(100.00*t.getGoodsNum()/totalNum).setScale(2, BigDecimal.ROUND_HALF_UP)+"";
				FlowDirectionAnalysisDto dto = new FlowDirectionAnalysisDto(t.getSenderAddrCity(),t.getReceiverAddrCity(),t.getGoodsNum(),zb);
				result.add(dto);
			}
		}else{
			String startCity = (String)params.get("startCity");
			if(!"全部".equals(startCity)){
				FlowDirectionAnalysisDto dto = new FlowDirectionAnalysisDto();
				dto.setSenderAddrCity(startCity);
				dto.setReceiverAddrCity("");
				dto.setZb("");
				result.add(dto);
			}
		}
		return result;
	}
	
	/**
	 * 获取大屏配置信息
	 * @param paramsMap
	 * @return
	 */
	public Map<String, Object> buildParams(Map<String, Object> paramsMap) {
		logger.info("获取大屏配置参数信息，屏幕号："+paramsMap.get("screenCode")+"******用户ID:"+paramsMap.get("userId"));
		Result<ScreenConfig> config = this.screenStatisticsService.getScreenConfig(Long.valueOf(paramsMap.get("userId")+""), paramsMap.get("screenCode")+"");
		Map<String,Object> params = new HashMap<>();
		if(config.isSuccess() && null != config.getData()) {
			ScreenConfig screenConfig = config.getData();
			params.put("startTm", screenConfig.getStartTm());
			
			//结束时间取当前时间
			params.put("endTm", screenConfig.getEndTm()==null?new Date():screenConfig.getEndTm());
			
			params.put("senderDeptIds", screenConfig.getSenderDeptIds());
			params.put("senderCustNos", screenConfig.getSenderCustNos());
			params.put("receiverCustNos", screenConfig.getReceiverCustNos());
			params.put("goodsIds", screenConfig.getGoodsIds());
			
			//接收单位ID
			params.put("receiverDeptIds", screenConfig.getReceiverDeptIds());
			
			//给流向使用的发货城市
			params.put("startCity", screenConfig.getStartCity());
			
			//没有物资ID就取t_despatch_batch表goods_num的数据
			if(null == screenConfig.getGoodsIds() || screenConfig.getGoodsIds().size() == 0) {
				params.put("shortTable", "tdb");
			}else {
				params.put("shortTable", "tdg");
			}
		}else {
			logger.error("获取大屏配置参数错误，屏幕号："+paramsMap.get("screenCode")+"******用户ID:"+paramsMap.get("userId"));
			return null;
		}
		return params;
	}
	
	/**
	 * 获取更新时间
	 * @return
	 */
	@Override
	public IndexRankTop5Version findUpdateDate() {
		
		return indexRankTop5VersionDao.getNewestVersionRecord();
	}


}
