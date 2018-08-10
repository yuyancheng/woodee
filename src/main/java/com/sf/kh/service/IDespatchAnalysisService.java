package com.sf.kh.service;

import java.util.List;
import java.util.Map;

import com.sf.kh.dto.ArrivalAnalysisDto;
import com.sf.kh.dto.EffectivenessAnalysisDto;
import com.sf.kh.dto.FlowDirectionAnalysisDto;
import com.sf.kh.model.IndexRankTop5Version;

/**
 * 配送分析、流向分析大屏幕
 * @author 835089
 *
 */
public interface IDespatchAnalysisService {

	/**
	 * 顺丰发货总量,分开的总量(在途/已签收/正在派送/其他)
	 * @param paramMap
	 * @return
	 */
	List<ArrivalAnalysisDto> findArrivalAnalysisByCond(Map<String, Object> paramsMap);
	
	/**
	 * 时效分析
	 * @param paramMap
	 * @return
	 */
	List<EffectivenessAnalysisDto> findEffectivenessAnalysisByCond(Map<String, Object> paramsMap);
	
	/**
	 * 异常件分析
	 * @param paramMap
	 * @return
	 */
	Map<String, Object> findExceptionalAnalysisByCond(Map<String, Object> paramsMap);
	
	/**
	 * 流向分析
	 * @param paramMap
	 * @return
	 */
	List<FlowDirectionAnalysisDto> findFlowDirectionAnalysisBycond(Map<String, Object> paramsMap);
	
	/**
	 * 获得大屏配置细腻系
	 * @param paramsMap
	 * @return
	 */
	Map<String, Object> buildParams(Map<String, Object> paramsMap);
	
	/**
	 * 获取大屏更新时间
	 * @return
	 */
	IndexRankTop5Version findUpdateDate();
}
