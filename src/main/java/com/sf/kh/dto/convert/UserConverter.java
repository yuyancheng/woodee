package com.sf.kh.dto.convert;

import static code.ponfee.commons.util.SpringContextHolder.getBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.sf.kh.dto.OrgDto;
import com.sf.kh.dto.UserDto;
import com.sf.kh.enums.UserType;
import com.sf.kh.model.Department;
import com.sf.kh.model.Organization;
import com.sf.kh.model.OrganizationType;
import com.sf.kh.model.Role;
import com.sf.kh.model.User;
import com.sf.kh.model.UserDept;
import com.sf.kh.service.IDepartmentService;
import com.sf.kh.service.IOrganizationTypeService;

import code.ponfee.commons.model.AbstractDataConverter;

/**
 * Converts User model to UserDto 
 * 
 * @author 01367825
 */
public class UserConverter extends AbstractDataConverter<User, UserDto> {

    @Override
    public UserDto convert(User user) {
        UserDto dto = super.convert(user);
        if (CollectionUtils.isNotEmpty(user.getRoles())) {
            Role role = user.getRoles().get(0); // 目前对应一个角色
            dto.setRoleId(role.getId());
            dto.setRoleCode(role.getRoleCode());
            dto.setRoleName(role.getRoleName());
        }
        if (user.getDept() != null) {
            Department dept = user.getDept();

            dto.setDeptId(dept.getId());
            dto.setDeptName(dept.getDeptName());
            dto.setParentDeptId(dept.getParentDeptId());
            dto.setTopDeptId(dept.getTopDeptId());
            dto.setOrgId(dept.getOrgId());

            dto.setOrgTypeId(dept.getOrgTypeId());
            OrganizationType orgType = getBean(IOrganizationTypeService.class)
                                          .getOrgTypeById(dept.getOrgTypeId());
            if (orgType != null) {
                dto.setOrgMaterialMark(orgType.getMaterialMark());
            }

            dto.setProvinceName(dept.getProvinceName());
            dto.setCityName(dept.getCityName());
            dto.setAreaName(dept.getAreaName());
            dto.setAddressDetail(dept.getAddressDetail());

            dto.setDeptPath(dept.getDeptPath());
            dto.setHeadQuarter(dept.getHeadQuarter());

            List<String> custNoList = new ArrayList<>();
            if (StringUtils.isNotBlank(dept.getCustNo())) {
                custNoList.add(dept.getCustNo());
            }
            if (user.getDeptPurview() == UserDept.PURVIEW_ALL) {
                List<Department> list = getBean(IDepartmentService.class)
                                            .getAllChildDepts(dept.getId());
                if (CollectionUtils.isNotEmpty(list)) {
                    for (Department d : list) {
                        if (StringUtils.isNotBlank(d.getCustNo())) {
                            custNoList.add(d.getCustNo());
                        }
                    }
                }
            }
            dto.setCustNoList(custNoList);

            dto.setDeptValid(dept.getValid());

            if (dept.getOrganization() != null) {
                List<OrgDto> orgPathList = new ArrayList<>();
                buildOrgPath(dept.getOrganization(), orgPathList);
                Collections.reverse(orgPathList);
                dto.setOrgPathList(orgPathList);
            }
        }

        dto.setUserType(UserType.from(dto));
        return dto;
    }

    private void buildOrgPath(Organization org, List<OrgDto> list) {
        OrgDto dto = new OrgDto();
        dto.setOrgId(org.getId());
        dto.setOrgName(org.getOrgName());
        dto.setParentOrgId(org.getParentOrgId());
        dto.setTopOrgId(org.getTopOrgId());
        dto.setValid(org.getValid());
        dto.setOrgTypeId(org.getOrgTypeId());
        if (org.getOrganizationType() != null) {
            dto.setOrgTypeName(org.getOrganizationType().getOrgTypeName());
        }
        dto.setLevelId(org.getLevelId());
        dto.setLevelName(org.getLevelName());
        dto.setOrgPath(org.getOrgPath());
        list.add(dto);
        if (org.getParentOrganization() != null) {
            buildOrgPath(org.getParentOrganization(), list);
        }
    }
}
