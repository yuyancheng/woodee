package com.sf.kh.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;
import java.util.List;

public class Department {

    private Long id;

    private String deptName;

    private Long parentDeptId;

    private Long topDeptId;

    private Long orgId;

    private Long orgTypeId;

    private String provinceName;

    private String cityName;

    private String areaName;

    private String addressDetail;
    /**
     * 1 有效
     * 2 無效
     */
    private Integer valid;

    /**
     * 1 是部门的总部
     * 2 非部门的总部
     */
    private Integer headQuarter;

    /**
     * 月结单号
     */
    private String custNo;

    private String deptPath;

    private Long createBy;

    private Date createTm;

    private Long updateBy;

    private Date updateTm;

    private Integer version;

    /**
     * 供 统计使用的名称
     */
    private String biRelName;

    /**
     * 组织
     */
    private Organization organization;

    public Department() {}

    public Department(Long id) {
        this.id = id;
    }

    private List<Long> deptIds;

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
        this.deptName = deptName == null ? null : deptName.trim();
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


    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo == null ? null : custNo.trim();
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

    public Integer getHeadQuarter() {
        return headQuarter;
    }

    public void setHeadQuarter(Integer headQuarter) {
        this.headQuarter = headQuarter;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public Long getOrgTypeId() {
        return orgTypeId;
    }

    public void setOrgTypeId(Long orgTypeId) {
        this.orgTypeId = orgTypeId;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getDeptPath() {
        return deptPath;
    }

    public void setDeptPath(String deptPath) {
        this.deptPath = deptPath;
    }

    public List<Long> getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(List<Long> deptIds) {
        this.deptIds = deptIds;
    }


    public String getBiRelName() {
        return biRelName;
    }

    public void setBiRelName(String biRelName) {
        this.biRelName = biRelName;
    }

    public enum HeadQuarterEnum{

        YES(1), NO(2);

        private int code;

        HeadQuarterEnum(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}