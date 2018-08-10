package com.sf.kh.model;

import java.io.Serializable;

/***
 * 物资组织关联表
 * @author 866316
 *
 */
public class GoodsDept implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Long parentOrgId;//专业局组织id

    private Long orgId;//业务处组织id
    
    private Long parentCategoryId;//第一分类

    private Long categoryId;//第二分类

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getParentOrgId() {
		return parentOrgId;
	}

	public void setParentOrgId(Long parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(Long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	} 
}