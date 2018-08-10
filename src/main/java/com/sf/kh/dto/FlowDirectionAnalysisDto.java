package com.sf.kh.dto;

import java.io.Serializable;

/**
 * 流向分析
 * @author 835089
 *
 */
public class FlowDirectionAnalysisDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3099548332530396275L;
	
	/**
	 * 发货城市
	 */
	private String senderAddrCity;
	
	/**
	 * 目的地城市
	 */
	private String receiverAddrCity;
	
	/**
	 * 货物数量
	 */
	private Long goodsNum;
	
	/**
	 * 占比
	 */
	private String zb;
	

	public FlowDirectionAnalysisDto() {
		super();
	}
	
	public FlowDirectionAnalysisDto(String senderAddrCity, String receiverAddrCity, Long goodsNum, String zb) {
		super();
		this.senderAddrCity = senderAddrCity;
		this.receiverAddrCity = receiverAddrCity;
		this.goodsNum = goodsNum;
		this.zb = zb;
	}

	public Long getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Long goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getZb() {
		return zb;
	}

	public void setZb(String zb) {
		this.zb = zb;
	}

	public String getReceiverAddrCity() {
		return receiverAddrCity;
	}

	public void setReceiverAddrCity(String receiverAddrCity) {
		this.receiverAddrCity = receiverAddrCity;
	}

	public String getSenderAddrCity() {
		return senderAddrCity;
	}

	public void setSenderAddrCity(String senderAddrCity) {
		this.senderAddrCity = senderAddrCity;
	}
	
}
