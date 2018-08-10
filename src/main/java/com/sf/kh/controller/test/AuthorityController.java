package com.sf.kh.controller.test;

import java.io.PrintWriter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

//import com.sf.kh.auth.test.Authority;
import com.sf.kh.model.test.Person;

/**
 * http://localhost:8000/ess-palp-core/auth/within
 * http://localhost:8000/ess-palp-core/auth/without
 */
@Controller()
@RequestMapping("/auth")
public class AuthorityController {

    /**
     * 测试post请求
     * @param person
     * @param pw
     */
//    @Authority(entrust = "within")
    @RequestMapping("/entrust")
    public void entrust(Person person, PrintWriter pw) {
        System.out.println(person.getName());
        pw.write("age " + person.getAge());
    }

    /**
     * 测试spring页面跳转
     * @param map
     * @return
     */
//    @Authority
    @RequestMapping("/within")
    public String within(ModelMap map) {
        map.put("key", "value");
        return "person.ftl";
    }

    @RequestMapping("/without")
    public String without(ModelMap map) {
        map.put("key", "value");
        return "person.ftl";
    }

}
