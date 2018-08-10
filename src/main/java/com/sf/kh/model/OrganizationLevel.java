package com.sf.kh.model;

import java.util.Date;

public class OrganizationLevel {

    private Long id;

    private String levelName;

    private Long orgTypeId;

    private Long parentLevelId;

    private Long createBy;

    private Date createTm;

    private Long updateBy;

    private Date updateTm;

    private Integer version;

    OrganizationLevel subLevel;

    OrganizationLevel parentLevel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName == null ? null : levelName.trim();
    }

    public Long getOrgTypeId() {
        return orgTypeId;
    }

    public void setOrgTypeId(Long orgTypeId) {
        this.orgTypeId = orgTypeId;
    }

    public Long getParentLevelId() {
        return parentLevelId;
    }

    public void setParentLevelId(Long parentLevelId) {
        this.parentLevelId = parentLevelId;
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

    public OrganizationLevel getSubLevel() {
        return subLevel;
    }

    public void setSubLevel(OrganizationLevel subLevel) {
        this.subLevel = subLevel;
    }

    public OrganizationLevel getParentLevel() {
        return parentLevel;
    }

    public void setParentLevel(OrganizationLevel parentLevel) {
        this.parentLevel = parentLevel;
    }
}