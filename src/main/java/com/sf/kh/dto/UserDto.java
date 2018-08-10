package com.sf.kh.dto;

import java.util.List;

import com.sf.kh.enums.UserType;

/**
 * The user data transfer object
 * 
 * @author 01367825
 */
public class UserDto implements java.io.Serializable {

    private static final long serialVersionUID = 2257719461207975861L;

    private Long id; // 用户ID
    private String username; // 用户名
    private String nickname; // 昵称（姓名）
    private String mobilePhone; // 手机号码
    private Boolean prompt; // 是否需要提示修改信息：1是；0否；

    // ------------------------------------角色
    private Long roleId;
    private String roleCode;
    private String roleName;

    // ------------------------------------单位信息
    private Long deptId;
    private String deptName;
    private Long parentDeptId;
    private Long topDeptId;
    private Integer deptPurview; // 单位权限：1普通；2管理；
    private Long orgTypeId; // 组织类型ID
    private Integer orgMaterialMark; // 是否物资管理标识：1是；2否；
    private Long orgId; // 组织ID

    // ------------------------------------单位地址
    private String provinceName;
    private String cityName;
    private String areaName;
    private String addressDetail;

    // ------------------------------------单位其它
    private List<String> custNoList;
    private Integer headQuarter;
    private String deptPath;
    private Integer deptValid; // 状态：1有效；2无效；

    // -----------------------------------单位所属的机构路径
    private List<OrgDto> orgPathList;

    // -----------------------------------getter/setter
    private UserType userType;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public void setPrompt(Boolean prompt) {
        this.prompt = prompt;
    }

    public Boolean getPrompt() {
        return prompt;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
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

    public Long getOrgTypeId() {
        return orgTypeId;
    }

    public void setOrgTypeId(Long orgTypeId) {
        this.orgTypeId = orgTypeId;
    }

    public List<String> getCustNoList() {
        return custNoList;
    }

    public void setCustNoList(List<String> custNoList) {
        this.custNoList = custNoList;
    }

    public Integer getHeadQuarter() {
        return headQuarter;
    }

    public void setHeadQuarter(Integer headQuarter) {
        this.headQuarter = headQuarter;
    }

    public String getDeptPath() {
        return deptPath;
    }

    public void setDeptPath(String deptPath) {
        this.deptPath = deptPath;
    }

    public Integer getDeptValid() {
        return deptValid;
    }

    public void setDeptValid(Integer deptValid) {
        this.deptValid = deptValid;
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

    public List<OrgDto> getOrgPathList() {
        return orgPathList;
    }

    public void setOrgPathList(List<OrgDto> orgPathList) {
        this.orgPathList = orgPathList;
    }

    public Integer getOrgMaterialMark() {
        return orgMaterialMark;
    }

    public void setOrgMaterialMark(Integer orgMaterialMark) {
        this.orgMaterialMark = orgMaterialMark;
    }

    public Integer getDeptPurview() {
        return deptPurview;
    }

    public void setDeptPurview(Integer deptPurview) {
        this.deptPurview = deptPurview;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
