package com.sf.kh.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import com.sf.kh.dto.UserDto;
import com.sf.kh.dto.convert.UserConverter;
import com.sf.kh.model.User;
import com.sf.kh.service.IDepartmentService;

import code.ponfee.commons.util.SpringContextHolder;

/**
 * The web context holder
 * 
 * @author 01367825
 */
public class WebContextHolder {

    private static final String CURRENT_USER = "current_user";

    public static void currentUser(User user) {
        // TODO this place can prehandle the request user info
        getRequest().setAttribute(CURRENT_USER, user/*.mask()*/);
    }

    public static User currentUser() {
        return (User) getRequest().getAttribute(CURRENT_USER);
    }

    public static UserDto currentUserDto() {
        User user = currentUser();
        if (user.getDeptId() != null && user.getDeptId() > 0) {
            IDepartmentService deptService = SpringContextHolder.getBean(IDepartmentService.class);
            user.setDept(deptService.getDeptWithHierarchicalOrgById(user.getDeptId()));
        }
        return new UserConverter().convert(user);
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletWebRequest) RequestContextHolder.getRequestAttributes()).getResponse();
    }
}
