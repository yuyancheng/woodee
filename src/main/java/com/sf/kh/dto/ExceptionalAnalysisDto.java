package com.sf.kh.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 异常分析
 * @author 835089
 *
 */
public class ExceptionalAnalysisDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2799096376766320922L;

	/**
	 * 快件单号
	 */
	private String waybillNo;
	
	/**
	 * 快件状态
	 */
	private String waybillStatus;
	
	/**
	 * 寄件时间
	 */
	private Date consignedTime;
	
	/**
	 * 发货城市
	 */
	private String senderCity;
	
	/**
	 * 目的城市
	 */
	private String destCity;
	
	/**
	 * 对应状态
	 */
	private String stat;

	public String getWaybillStatus() {
		return waybillStatus;
	}

	public void setWaybillStatus(String waybillStatus) {
		this.waybillStatus = waybillStatus;
	}

	public Date getConsignedTime() {
		return consignedTime;
	}

	public void setConsignedTime(Date consignedTime) {
		this.consignedTime = consignedTime;
	}

	public String getSenderCity() {
		return senderCity;
	}

	public void setSenderCity(String senderCity) {
		this.senderCity = senderCity;
	}

	public String getDestCity() {
		return destCity;
	}

	public void setDestCity(String destCity) {
		this.destCity = destCity;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	
	
	
}
