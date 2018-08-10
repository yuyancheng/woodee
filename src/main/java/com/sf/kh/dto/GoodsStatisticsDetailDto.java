package com.sf.kh.dto;

import java.io.Serializable;

/***
 * 物资统计-物资明细[发运物资详情/接收物资详情]
 * @author 866316
 */
public class GoodsStatisticsDetailDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String goodsCode;
	
	private String goodsName;
	
	private String goodsUnit;
	
	private String firstCategoryName;
	
	private String secondCategoryName;
	
	private Integer sendCount=0;
	
	private Integer unReceiveCount=0;
	
	private Integer receiveCount=0;
	
	private Double receiveRate=0.0D;

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsUnit() {
		return goodsUnit;
	}

	public void setGoodsUnit(String goodsUnit) {
		this.goodsUnit = goodsUnit;
	}

	public String getFirstCategoryName() {
		return firstCategoryName;
	}

	public void setFirstCategoryName(String firstCategoryName) {
		this.firstCategoryName = firstCategoryName;
	}

	public String getSecondCategoryName() {
		return secondCategoryName;
	}

	public void setSecondCategoryName(String secondCategoryName) {
		this.secondCategoryName = secondCategoryName;
	}

	public Integer getSendCount() {
		return sendCount;
	}

	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}

	public Integer getUnReceiveCount() {
		return unReceiveCount;
	}

	public void setUnReceiveCount(Integer unReceiveCount) {
		this.unReceiveCount = unReceiveCount;
	}

	public Integer getReceiveCount() {
		return receiveCount;
	}

	public void setReceiveCount(Integer receiveCount) {
		this.receiveCount = receiveCount;
	}

	public Double getReceiveRate() {
		return receiveRate;
	}

	public void setReceiveRate(Double receiveRate) {
		this.receiveRate = receiveRate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
