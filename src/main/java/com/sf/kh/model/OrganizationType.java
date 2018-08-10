package com.sf.kh.model;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Date;


public class OrganizationType implements Serializable {

    private Long id;

    private String orgTypeName;

    private Integer valid;

    private Integer materialMark;

    private Integer biRelType;

    private Long createBy;

    private Date createTm;

    private Long updateBy;

    private Date updateTm;

    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgTypeName() {
        return orgTypeName;
    }

    public void setOrgTypeName(String orgTypeName) {
        this.orgTypeName = orgTypeName == null ? null : orgTypeName.trim();
    }

    public Integer getBiRelType() {
        return biRelType;
    }

    public void setBiRelType(Integer biRelType) {
        this.biRelType = biRelType;
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

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
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

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public Integer getMaterialMark() {
        return materialMark;
    }

    public void setMaterialMark(Integer materialMark) {
        this.materialMark = materialMark;
    }

    public static void main(String[] args) {
        OrganizationType type = new OrganizationType();
        type.setId(2L);
        type.setOrgTypeName("新组织名称");
        System.out.println(JSONObject.toJSONString(type));
    }
}

