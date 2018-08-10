package com.sf.kh.dto;
/**
 * 物资统计-方向
 * @author 866316
 */
public class DepartmentStatisticsDto implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String deptName = "";
	
	private Integer deptAmount = 0;
    
    private Double deptRate = 0.0D;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Double getDeptRate() {
		return deptRate;
	}

	public void setDeptRate(Double deptRate) {
		this.deptRate = deptRate;
	}

	public Integer getDeptAmount() {
		return deptAmount;
	}

	public void setDeptAmount(Integer deptAmount) {
		this.deptAmount = deptAmount;
	}
}
