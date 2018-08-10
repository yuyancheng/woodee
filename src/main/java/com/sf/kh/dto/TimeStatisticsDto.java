package com.sf.kh.dto;
/**
 * 物资统计-时间dto
 * @author 866316
 */
public class TimeStatisticsDto implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String month;
	
	private Integer sendAmount = 0;
	
	private Integer receiveAmount = 0;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Integer getSendAmount() {
		return sendAmount;
	}

	public void setSendAmount(Integer sendAmount) {
		this.sendAmount = sendAmount;
	}

	public Integer getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(Integer receiveAmount) {
		this.receiveAmount = receiveAmount;
	}
}
