package com.sf.kh.dto;

import java.io.Serializable;

/**
 * 到货分析
 * @author 835089
 *
 */
public class ArrivalAnalysisDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3128714989065278025L;

	/**
	 * 快件状态
	 */
	private String waybillStatus;
	
	/**
	 * 快件总量
	 */
	private Long goodsNum;
	
	public ArrivalAnalysisDto() {
		super();
	}

	public ArrivalAnalysisDto(String waybillStatus, Long goodsNum) {
		super();
		this.waybillStatus = waybillStatus;
		this.goodsNum = goodsNum;
	}

	public String getWaybillStatus() {
		return waybillStatus;
	}

	public void setWaybillStatus(String waybillStatus) {
		this.waybillStatus = waybillStatus;
	}

	public Long getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Long goodsNum) {
		this.goodsNum = goodsNum;
	}
	
	
}
