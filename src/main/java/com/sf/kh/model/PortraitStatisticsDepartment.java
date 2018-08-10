package com.sf.kh.model;

import java.io.Serializable;

/***
 * 物资纵向统计-方向单位占比
 * @author 866316
 *
 */
public class PortraitStatisticsDepartment implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long id;

    private Integer type;

    private String typeName;

    private String receiveCompanyId;

    private Long goodsNum;

    private Integer version;

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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public String getReceiveCompanyId() {
        return receiveCompanyId;
    }

    public void setReceiveCompanyId(String receiveCompanyId) {
        this.receiveCompanyId = receiveCompanyId == null ? null : receiveCompanyId.trim();
    }

    public Long getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Long goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}