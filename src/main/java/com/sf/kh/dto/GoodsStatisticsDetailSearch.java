package com.sf.kh.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/***
 * 物资统计-物资明细[发运物资详情/接收物资详情]--查询参数
 * @author 866316
 */
public class GoodsStatisticsDetailSearch implements Serializable{

	private static final long serialVersionUID = 1L;

	private List<Long> sendDeptId = new ArrayList<Long>();
	
	private List<Long> receiveDeptId = new ArrayList<Long>();
	
	private List<Long> goodsId = new ArrayList<Long>();
	
	private String startDate;
	
	private String endDate;

	public List<Long> getSendDeptId() {
		return sendDeptId;
	}

	public void setSendDeptId(List<Long> sendDeptId) {
		this.sendDeptId = sendDeptId;
	}

	public List<Long> getReceiveDeptId() {
		return receiveDeptId;
	}

	public void setReceiveDeptId(List<Long> receiveDeptId) {
		this.receiveDeptId = receiveDeptId;
	}

	public List<Long> getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(List<Long> goodsId) {
		this.goodsId = goodsId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
