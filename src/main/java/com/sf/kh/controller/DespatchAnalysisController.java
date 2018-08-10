package com.sf.kh.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sf.kh.dto.ArrivalAnalysisDto;
import com.sf.kh.dto.EffectivenessAnalysisDto;
import com.sf.kh.dto.FlowDirectionAnalysisDto;
import com.sf.kh.model.IndexRankTop5Version;
import com.sf.kh.model.ScreenConfig;
import com.sf.kh.model.User;
import com.sf.kh.service.IDespatchAnalysisService;
import com.sf.kh.util.WebContextHolder;

import code.ponfee.commons.model.Result;

/**
 * 配送分析和流向分析大屏展示
 * @author 835089
 *
 */
@RestController
@RequestMapping(path = "despatchAnalysis")
public class DespatchAnalysisController {
	
	private static Logger logger = LoggerFactory.getLogger(DespatchAnalysisController.class);

	@Resource
	private IDespatchAnalysisService despatchAnalysisService;
	
	@PostMapping(path = "findResult")
	public Result<Map<String,Object>> findResult(HttpSession session,@RequestParam(value = "screenCode", required = true) String screenCode){
		

		User user = WebContextHolder.currentUser();
		logger.info("enter despatchAnalysis/findResult,screenCode is:"+screenCode);
		
		//参数构造
		Map<String,Object> paramsMap = new HashMap<>();
		paramsMap.put("screenCode", screenCode);
		paramsMap.put("userId", user.getId());
		
		//获取配置信息，如果没有，则返回404
		Map<String,Object> parameterMap = this.despatchAnalysisService.buildParams(paramsMap);
		if(null == parameterMap) {
			return Result.failure(404, "还未设置大屏");
		}
		
		Map<String,Object> results = new HashMap<>();
		if(!ScreenConfig.SCREEN_FLOW.equals(screenCode)) {
			
			List<ArrivalAnalysisDto> arrivalAnalysisList = this.despatchAnalysisService.findArrivalAnalysisByCond(paramsMap);
			List<EffectivenessAnalysisDto> effectivenessAnalysisList = this.despatchAnalysisService.findEffectivenessAnalysisByCond(paramsMap);
			Map<String,Object> exceptionalAnalysisMap = this.despatchAnalysisService.findExceptionalAnalysisByCond(paramsMap);
			
			results.put("arrivalAnalysisList", arrivalAnalysisList);
			results.put("effectivenessAnalysisList", effectivenessAnalysisList);
			results.put("exceptionalAnalysisMap", exceptionalAnalysisMap);
		}
		
		List<FlowDirectionAnalysisDto> flowDirectionAnalysisList = this.despatchAnalysisService.findFlowDirectionAnalysisBycond(paramsMap);
		results.put("flowDirectionAnalysisList", flowDirectionAnalysisList);
		
		logger.info("end despatchAnalysis/findResult,username is:"+user.getUsername());
		return Result.success(results);
	}
	
	@PostMapping(path = "findUpdateDate")
	public Result<IndexRankTop5Version> findUpdateDate(){
		return Result.success(this.despatchAnalysisService.findUpdateDate());
	}
}

