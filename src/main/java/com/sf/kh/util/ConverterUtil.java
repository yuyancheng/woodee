package com.sf.kh.util;

import code.ponfee.commons.model.ResultCode;
import com.google.common.collect.Maps;
import com.sf.kh.exception.BusinessException;
import com.sf.kh.model.Department;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * @Auther: 01378178
 * @Date: 2018/6/20 09:55
 * @Description:
 */
public class ConverterUtil {


    private static final Logger logger = LoggerFactory.getLogger(ConverterUtil.class);

    /**
     * 将对象转换为Map, 确保目标field 有对应的get方法. 适用于pojo
     * @param t
     * @param <T>
     * @return
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T> Map<String, Object> convertToMap(T t){

        try {

            BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
            MethodDescriptor[] descriptors = beanInfo.getMethodDescriptors();

            Map<String, Object> rs = Maps.newHashMap();

            for (MethodDescriptor descriptor : descriptors) {
                Method method = descriptor.getMethod();
                String name = descriptor.getMethod().getName();

                if (StringUtils.startsWith(name, "get") && !StringUtils.equals(name, "getClass")) {
                    Object val = method.invoke(t);
                    String key = StringUtils.uncapitalize(name.substring("get".length()));
                    if (val != null) {
                        rs.put(key, val);
                    }
                }
            }

            return rs;

        }catch(Exception e){
            logger.error("convert to map error.", e);
            throw new BusinessException(ResultCode.SERVER_ERROR.getCode(), "对象转换为map失败");
        }
    }


    public static void main(String[] args){
        Department dept = new Department();
        dept.setParentDeptId(0L);
        dept.setTopDeptId(1L);
        dept.setCreateBy(12321321L);
        dept.setCreateTm(new Date());
//        System.out.println(convertToMap(dept).toString());
    }
}
