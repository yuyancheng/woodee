package com.sf.kh.model;

import java.io.Serializable;
/***
 * 物资统计-时间维度
 * @author 866316
 *
 */
public class TimeStatisticsVersionData implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Long sendDeptId;
	
	private Long receiveDeptId;

    private String month;

    private Integer amount;

    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
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

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
}