package com.sf.kh.dto;
/**
 * 物资统计-专业
 * @author 866316
 */
public class CategoryStatisticsDto implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String categoryName;
	
	private Integer sendAmount = 0;
	
	private Integer receiveAmount = 0;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
