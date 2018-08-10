package com.sf.kh.dto;

/**
 * Org data transform object
 * 
 * @author 01367825
 */
public class OrgDto implements java.io.Serializable {

    private static final long serialVersionUID = -298479468232825690L;

    private Long orgId; // 组织ID
    private String orgName; // 组织名称
    private Long parentOrgId; // 父级组织ID
    private Long topOrgId; // 顶级组织ID
    private Integer valid; // 是否有效

    private Long orgTypeId; // 组织类型ID
    private String orgTypeName; // 组织类型名称

    private Long levelId; // 层级ID
    private String levelName; // 层级名称

    private String orgPath; // 组织上下级关系路径

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(Long parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public Long getTopOrgId() {
        return topOrgId;
    }

    public void setTopOrgId(Long topOrgId) {
        this.topOrgId = topOrgId;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public Long getOrgTypeId() {
        return orgTypeId;
    }

    public void setOrgTypeId(Long orgTypeId) {
        this.orgTypeId = orgTypeId;
    }

    public String getOrgTypeName() {
        return orgTypeName;
    }

    public void setOrgTypeName(String orgTypeName) {
        this.orgTypeName = orgTypeName;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getOrgPath() {
        return orgPath;
    }

    public void setOrgPath(String orgPath) {
        this.orgPath = orgPath;
    }

}
