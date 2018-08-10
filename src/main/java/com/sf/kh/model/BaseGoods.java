package com.sf.kh.model;

import java.io.Serializable;
/***
 * 物资基础维表
 * @author 866316
 *
 */
public class BaseGoods implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Long goodsDeptId;//物资部门类别关联表id

    private String goodsCode;//物资编码

    private String goodsName;//物资名称

    private String goodsUnit;//物资单位

    private Long createBy;

    private String createTm;

    private String updateTm;
    
    private Integer valid;//1,有效；2：无效且被删除

    private Integer version;
    
    private Long professionDeptId;//专业局id
    
    private String professionDeptName;//专业局名称
    
    private Long operatingDeptId;//业务处id

    private String operatingDeptName;//业务部门名称
    
    private Long firstCategoryId;//类别1的id
    
    private String firstCategoryName;//类别1名称
    
    private Long secondCategoryId;//类别2的id
    
    private String secondCategoryName;//类别2名称
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit == null ? null : goodsUnit.trim();
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getCreateTm() {
		return createTm;
	}

	public void setCreateTm(String createTm) {
		this.createTm = createTm;
	}

	public String getUpdateTm() {
		return updateTm;
	}

	public void setUpdateTm(String updateTm) {
		this.updateTm = updateTm;
	}

	public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

	public Long getFirstCategoryId() {
		return firstCategoryId;
	}

	public void setFirstCategoryId(Long firstCategoryId) {
		this.firstCategoryId = firstCategoryId;
	}

	public Long getSecondCategoryId() {
		return secondCategoryId;
	}

	public void setSecondCategoryId(Long secondCategoryId) {
		this.secondCategoryId = secondCategoryId;
	}

	public Long getGoodsDeptId() {
		return goodsDeptId;
	}

	public void setGoodsDeptId(Long goodsDeptId) {
		this.goodsDeptId = goodsDeptId;
	}

	public Long getProfessionDeptId() {
		return professionDeptId;
	}

	public void setProfessionDeptId(Long professionDeptId) {
		this.professionDeptId = professionDeptId;
	}

	public String getProfessionDeptName() {
		return professionDeptName;
	}

	public void setProfessionDeptName(String professionDeptName) {
		this.professionDeptName = professionDeptName;
	}

	public Long getOperatingDeptId() {
		return operatingDeptId;
	}

	public void setOperatingDeptId(Long operatingDeptId) {
		this.operatingDeptId = operatingDeptId;
	}

	public String getOperatingDeptName() {
		return operatingDeptName;
	}

	public void setOperatingDeptName(String operatingDeptName) {
		this.operatingDeptName = operatingDeptName;
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

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}	
}