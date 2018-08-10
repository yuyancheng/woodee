package com.sf.kh.startup;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.sf.kh.model.Role;
import com.sf.kh.service.IRoleService;
import com.sf.kh.util.Constants;

import code.ponfee.commons.util.SpringContextHolder;

/**
 * spring容器启动完成后执行
 * 
 * @author Ponfee
 */
@Component
public class SpringStartupListener implements ApplicationListener<ContextRefreshedEvent> {

    private static long roleManager;
    private static long roleGeneral;

    /**
     * spring初始化完成后执行一次
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // check the container is root container
        if (event.getApplicationContext().getParent() == null) {
            // do something for application init
            //System.out.println("*******************SpringStartupListener init2*******************");
            IRoleService roleService = SpringContextHolder.getBean(IRoleService.class);
            Role manager = roleService.getByRoleCode(Constants.ROLE_MANAGER).getData();
            Role general = roleService.getByRoleCode(Constants.ROLE_GENERAL).getData();
            roleManager = manager != null ? manager.getId() : 0;
            roleGeneral = general != null ? general.getId() : 0;
        }
    }

    public static long roleManager() {
        return roleManager;
    }

    public static long roleGeneral() {
        return roleGeneral;
    }
}
