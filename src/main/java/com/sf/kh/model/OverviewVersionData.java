package com.sf.kh.model;

import java.io.Serializable;

public class OverviewVersionData implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private Long sendCompanyId;
	
	private String sendCompanyName;
	
	private Long receiveCompanyId;
	
	private String receiveCompanyName;
	
	private Integer amount;
	
	private Double cubage;
	
	private Double weight;
	
	private Integer version;
	
	private String date;
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Long getSendCompanyId() {
		return sendCompanyId;
	}

	public void setSendCompanyId(Long sendCompanyId) {
		this.sendCompanyId = sendCompanyId;
	}

	public String getSendCompanyName() {
		return sendCompanyName;
	}

	public void setSendCompanyName(String sendCompanyName) {
		this.sendCompanyName = sendCompanyName;
	}

	public Long getReceiveCompanyId() {
		return receiveCompanyId;
	}

	public void setReceiveCompanyId(Long receiveCompanyId) {
		this.receiveCompanyId = receiveCompanyId;
	}

	public String getReceiveCompanyName() {
		return receiveCompanyName;
	}

	public void setReceiveCompanyName(String receiveCompanyName) {
		this.receiveCompanyName = receiveCompanyName;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Double getCubage() {
		return cubage;
	}

	public void setCubage(Double cubage) {
		this.cubage = cubage;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}