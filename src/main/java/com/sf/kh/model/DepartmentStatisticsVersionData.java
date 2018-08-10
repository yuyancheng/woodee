package com.sf.kh.model;

import java.io.Serializable;

public class DepartmentStatisticsVersionData implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;

    private Long sendDeptId;
    
    private Long receiveDeptId;
    
    private Integer amount;

    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    } 

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

	public Long getSendDeptId() {
		return sendDeptId;
	}

	public void setSendDeptId(Long sendDeptId) {
		this.sendDeptId = sendDeptId;
	}

	public Long getReceiveDeptId() {
		return receiveDeptId;
	}

	public void setReceiveDeptId(Long receiveDeptId) {
		this.receiveDeptId = receiveDeptId;
	}
}