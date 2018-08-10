package com.sf.kh.dto;

/**
 * Dept dto
 * 
 * @author 01367825
 */
public class DeptDto implements java.io.Serializable {

    private static final long serialVersionUID = 2257719461207975861L;

    private Long id; // 单位ID
    private String deptName; // 单位名称
    private Long parentDeptId; // 父级单位ID
    private Long topDeptId; // 顶级单位ID
    private Long orgId; // 组织ID
    private String orgName; // 组织名称
    private Long orgTypeId; // 组织类型ID
    private String orgTypeName; // 组织类型名称
    private String provinceName; // 省份
    private String cityName; // 城市
    private String areaName; // 区县
    private String addressDetail; // 详细地址
    private Integer valid; // 是否有效：1有效；2无效；
    private String deptPath; // 单位关系路径
    private Long levelId; // 层级名称
    private String levelName; // 层级名称

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getParentDeptId() {
        return parentDeptId;
    }

    public void setParentDeptId(Long parentDeptId) {
        this.parentDeptId = parentDeptId;
    }

    public Long getTopDeptId() {
        return topDeptId;
    }

    public void setTopDeptId(Long topDeptId) {
        this.topDeptId = topDeptId;
    }

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

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public String getDeptPath() {
        return deptPath;
    }

    public void setDeptPath(String deptPath) {
        this.deptPath = deptPath;
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

}
