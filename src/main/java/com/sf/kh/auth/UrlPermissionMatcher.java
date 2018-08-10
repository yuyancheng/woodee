package com.sf.kh.auth;

import static code.ponfee.commons.util.SpringContextHolder.getBean;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.sf.kh.dto.PermitFlat;
import com.sf.kh.service.IPermitService;
import com.sf.kh.service.IUserService;

/**
 * Url permissions
 * 
 * @author 01367825
 */
public final class UrlPermissionMatcher {

    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    /** The system of permission urls of t_permit */
    private static Map<String, PermitFlat> permissions;

    /** Spring mapping urls of annotation RequestMapping */
    private static Set<String> mappings;

    public static boolean hasNotPermission(String url, long userId) {
        return !hasPermission(url, userId);
    }

    /**
     * 判断用户是否有该url的访问权限
     * 
     * @param url      the access url
     * @param userId   the username
     * @return {@code true} has permit
     */
    public static boolean hasPermission(String url, long userId) {
        Collection<String> userPermits = getBean(IUserService.class).queryUserPermits(userId).getData();
        if (userPermits == null) {
            userPermits = Collections.emptyList();
        }

        PermitFlat pf = null;
        boolean required = false; // 是否需要权限控制
        boolean hasPermit = false; // 是否有权限
        for (Iterator<Entry<String, PermitFlat>> i = permissions().entrySet().iterator(); i.hasNext();) {
            pf = i.next().getValue();
            if (StringUtils.isNotBlank(pf.getPermitUrl()) && MATCHER.match(pf.getPermitUrl(), url)) {
                required = true;
                if (pf.isAvailable() && userPermits.contains(pf.getPermitId())) {
                    hasPermit = true;
                    break;
                }
            }
        }

        return !required || hasPermit;
    }

    public static void invalidPermits() {
        permissions = null;
    }

    /**
     * Check is the spring mvc controller url mapping
     * 
     * @param url the user request url address
     * @return {@code true} is the spring mvc url mapping
     */
    public static boolean isMapping(String url) {
        Set<String> urls = mappings();
        if (urls == null) {
            return false;
        }

        for (String perm : urls) {
            if (MATCHER.match(perm, url)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNotMapping(String url) {
        return !isMapping(url);
    }

    /**
     * Returns the spring mvc request mapping urls
     * 
     * @return spring mvc request mapping urls set
     * @see org.springframework.web.bind.annotation.RequestMapping
     */
    private static Set<String> mappings() {
        Set<String> urls = mappings;
        if (urls == null) {
            synchronized (UrlPermissionMatcher.class) {
                if ((urls = mappings) == null) {
                    RequestMappingHandlerMapping m = getBean(RequestMappingHandlerMapping.class);
                    Map<RequestMappingInfo, HandlerMethod> map = m.getHandlerMethods();
                    ImmutableSet.Builder<String> builder = ImmutableSet.<String> builder();
                    for (RequestMappingInfo info : map.keySet()) {
                        builder.addAll(info.getPatternsCondition().getPatterns());
                    }
                    mappings = urls = builder.build();
                }
            }
        }
        return urls;
    }

    /**
     * Returns all the t_permit permissions data
     * 
     * @return Map<String, PermitFlat>
     */
    private static Map<String, PermitFlat> permissions() {
        Map<String, PermitFlat> permits = permissions;
        if (permits == null) {
            synchronized (UrlPermissionMatcher.class) {
                if ((permits = permissions) == null) {
                    IPermitService permitService = getBean(IPermitService.class);
                    List<PermitFlat> list = permitService.listAsFlat().getData();
                    Map<String, PermitFlat> map;
                    if (list == null) {
                        map = Collections.emptyMap();
                    } else {
                        ImmutableMap.Builder<String, PermitFlat> builder = new ImmutableMap.Builder<>();
                        for (PermitFlat pf : list) {
                            builder.put(pf.getPermitId(), pf);
                        }
                        map = builder.build();
                    }
                    permissions = permits = map;
                }
            }
        }
        return permits;
    }

}
