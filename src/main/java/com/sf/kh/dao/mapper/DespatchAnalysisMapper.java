package com.sf.kh.dao.mapper;

import java.util.List;
import java.util.Map;

import com.sf.kh.dto.ArrivalAnalysisDto;
import com.sf.kh.dto.EffectivenessAnalysisDto;
import com.sf.kh.dto.ExceptionalAnalysisDto;
import com.sf.kh.dto.FlowDirectionAnalysisDto;

/**
 * 配送分析大屏
 * @author 835089
 *
 */
public interface DespatchAnalysisMapper {

	/**
	 * 顺丰发货总量,分开的总量(在途/已签收/正在派送/其他)
	 * @param paramMap
	 * @return
	 */
	List<ArrivalAnalysisDto> findArrivalAnalysisByCond(Map<String, Object> paramMap);
	
	/**
	 * 时效分析
	 * @param paramMap
	 * @return
	 */
	List<EffectivenessAnalysisDto> findEffectivenessAnalysisByCond(Map<String, Object> paramMap);
	
	/**
	 * 异常件分析
	 * @param paramMap
	 * @return
	 */
	List<ExceptionalAnalysisDto> findExceptionalAnalysisByCond(Map<String, Object> paramMap);
	
	/**
	 * 流向分析
	 * @param paramMap
	 * @return
	 */
	List<FlowDirectionAnalysisDto> findFlowDirectionAnalysisBycond(Map<String, Object> paramMap);
}
