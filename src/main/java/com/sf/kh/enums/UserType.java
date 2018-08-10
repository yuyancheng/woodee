package com.sf.kh.enums;

import com.sf.kh.dto.UserDto;
import com.sf.kh.model.UserDept;
import com.sf.kh.util.Constants;

/**
 * 用户类型
 * 
 * @author 01367825
 */
public enum UserType {

    SYS_MANAGER("系统管理员"), //
    SPECIAL_TOP("业务局"), SPECIAL_BIZ("业务处"), //
    DEPT_HEAD("单位管理"), DEPT_SELF("单位普通");

    private final String desc;

    UserType(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return desc;
    }

    public static UserType from(UserDto u) {
        if (Constants.ROLE_MANAGER.equals(u.getRoleCode())) {
            return SYS_MANAGER;
        }

        if (u.getOrgMaterialMark() != null && u.getOrgMaterialMark() == 1) {
            if (u.getOrgPathList().size() == 1) {
                return SPECIAL_TOP; // 专业局
            } else {
                return SPECIAL_BIZ; // 业务处
            }
        }

        if (u.getDeptPurview() != null
            && u.getDeptPurview() == UserDept.PURVIEW_ALL) {
            return DEPT_HEAD;
        } else {
            return DEPT_SELF;
        }
    }
}
