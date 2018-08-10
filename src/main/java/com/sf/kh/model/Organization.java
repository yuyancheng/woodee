package com.sf.kh.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Transient;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

public class Organization {

    private Long id;

    private Long orgTypeId;

    private String orgName;

    private Integer valid;

    private Long parentOrgId;

    private Long topOrgId;

    private Integer sequence;

    private Long updateBy;

    private Long createBy;

    private Date updateTm;

    private Date createTm;

    /**
     * 组织级别名称
     */
    private String levelName;

    /**
     * 组织级别id
     */
    private Long levelId;


    private Integer version;

    private String orgPath;

    /**
     * 组织类型
     */
    private OrganizationType organizationType;

    /**
     * 父组织对象
     */
    private Organization parentOrganization;

    /**
     * 组织层级
     */
    private OrganizationLevel level;


    @Transient
    public static final String ORG_PATH_SEPARATOR = ":";

    public static String buildOrgPath(String parentOrgPath, String orgId){
        if(parentOrgPath==null || "".equals(parentOrgPath.trim())){
            return orgId;
        }
        return parentOrgPath+ORG_PATH_SEPARATOR+orgId;
    }

    public List<Long> splitOrgPath(){
        List<Long> orgIds = Lists.newArrayList();
        if(StringUtils.isBlank(this.getOrgPath())){
            return orgIds;
        }

        String[] array = this.getOrgPath().split(":");
        for(String s : array){
            orgIds.add(Long.valueOf(s));
        }
        return orgIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
    }


    public Long getOrgTypeId() {
        return orgTypeId;
    }

    public void setOrgTypeId(Long orgTypeId) {
        this.orgTypeId = orgTypeId;
    }

    public Long getTopOrgId() {
        return topOrgId;
    }

    public Long getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(Long parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public void setTopOrgId(Long topOrgId) {
        this.topOrgId = topOrgId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
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

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Organization getParentOrganization() {
        return parentOrganization;
    }

    public void setParentOrganization(Organization parentOrganization) {
        this.parentOrganization = parentOrganization;
    }

    public OrganizationLevel getLevel() {
        return level;
    }

    public void setLevel(OrganizationLevel level) {
        this.level = level;
    }

    public OrganizationType getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(OrganizationType organizationType) {
        this.organizationType = organizationType;
    }

    public String getOrgPath() {
        return orgPath;
    }

    public void setOrgPath(String orgPath) {
        this.orgPath = orgPath;
    }

    @JSONField(serialize=false)
    public boolean isTopOrg(){
        if(this.getParentOrgId()!=null && this.getParentOrgId().equals(0L)){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static void main(String[] args) {
       /* Organization org = new Organization();
        org.setOrgName("orgName1");
        org.setOrgTypeId(1L);
        org.setParentOrgId(0L);
        org.setSequence(1);
        System.out.println(JSONObject.toJSONString(org));*/
        System.out.println(Charset.defaultCharset());
    }
}