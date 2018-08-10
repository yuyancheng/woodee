package com.sf.kh.dto;

import java.io.Serializable;

/**
 * 时效分析
 * @author 835089
 *
 */
public class EffectivenessAnalysisDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1069409236163245729L;

	/**
	 * lt24/between24to48/between48to72/gt72   状态信息
	 */
	private String stat;
	
	/**
	 * 每种状态对应的数量
	 */
	private Long totalNum;
	
	/**
	 * 占比
	 */
	private String zb;
	
	public EffectivenessAnalysisDto() {
		super();
	}

	public EffectivenessAnalysisDto(String stat, String zb) {
		super();
		this.stat = stat;
		this.zb = zb;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public Long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}

	public String getZb() {
		return zb;
	}

	public void setZb(String zb) {
		this.zb = zb;
	}

}
