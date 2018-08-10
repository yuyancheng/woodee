package com.sf.kh.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/***
 * 字典表
 * @author 866316
 *
 */
public class BaseDict implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;

    private Integer type;

    private Long parentId;

    private String name;

    private Integer orderNum;

    private Long createBy;

    private Date createTm;

    private Date updateTm;

    private Integer version;
    
    private List<BaseDict> childrens;
    
    //物资可选,只有子集才有物资
    private List<BaseGoods> goods;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTm() {
        return createTm;
    }

    public void setCreateTm(Date createTm) {
        this.createTm = createTm;
    }

    public Date getUpdateTm() {
        return updateTm;
    }

    public void setUpdateTm(Date updateTm) {
        this.updateTm = updateTm;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

	public List<BaseDict> getChildrens() {
		if(this.childrens == null){
			this.childrens = new ArrayList<BaseDict>();
		}
		return this.childrens;
	}

	public void setChildrens(List<BaseDict> childrens) {
		this.childrens = childrens;
	}

	public List<BaseGoods> getGoods() {
		if(this.goods == null){
			this.goods = new ArrayList<BaseGoods>();
		}
		return goods;
	}

	public void setGoods(List<BaseGoods> goods) {
		this.goods = goods;
	}
}