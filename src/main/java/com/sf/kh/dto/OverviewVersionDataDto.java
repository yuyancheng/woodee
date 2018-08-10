package com.sf.kh.dto;

import java.io.Serializable;

public class OverviewVersionDataDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private Long deptId;

    private String deptName;

    private Long custNo;

    private Integer sendCount=0;

    private Double sendCubage=0.0D;

    private Double sendWeight=0.0D;

    private Integer receiveCount=0;

    private Double receiveCubage=0.0D;

    private Double receiveWeight=0.0D;
    
    private String date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public Double getSendCubage() {
        return sendCubage;
    }

    public void setSendCubage(Double sendCubage) {
        this.sendCubage = sendCubage;
    }

    public Double getSendWeight() {
        return sendWeight;
    }

    public void setSendWeight(Double sendWeight) {
        this.sendWeight = sendWeight;
    }

    public Integer getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(Integer receiveCount) {
        this.receiveCount = receiveCount;
    }

    public Double getReceiveCubage() {
        return receiveCubage;
    }

    public void setReceiveCubage(Double receiveCubage) {
        this.receiveCubage = receiveCubage;
    }

    public Double getReceiveWeight() {
        return receiveWeight;
    }

    public void setReceiveWeight(Double receiveWeight) {
        this.receiveWeight = receiveWeight;
    }

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}