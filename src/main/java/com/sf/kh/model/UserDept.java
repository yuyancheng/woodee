package com.sf.kh.model;

/**
 * The model class for table t_user_dept
 * 
 * @author Ponfee
 */
public class UserDept implements java.io.Serializable {

    private static final long serialVersionUID = 941323611957418083L;

    public static final int PURVIEW_SELF = 1; // 普通
    public static final int PURVIEW_ALL = 2; // 管理

    private Long userId; // 用户ID
    private Long deptId; // 单位ID
    private Integer deptPurview; // 单位权限：1普通；2管理；

    public UserDept() {}

    public UserDept(Long userId, Long deptId, Integer deptPurview) {
        this.userId = userId;
        this.deptId = deptId;
        this.deptPurview = deptPurview;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getDeptPurview() {
        return deptPurview;
    }

    public void setDeptPurview(Integer deptPurview) {
        this.deptPurview = deptPurview;
    }

}
