package com.sf.kh.model;

import java.io.Serializable;
/***
 * 物资统计-类别
 * @author 866316
 *
 */
public class CategoryStatisticsVersionData implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private Long sendDeptId;
    
    private Long receiveDeptId;

    private String categoryName;

    private Integer amount = 0;

    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
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