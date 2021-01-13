package com.powerlong.springmvc.init.handlerMapping;

import com.powerlong.springmvc.annotation.RequestMapping;
import com.powerlong.springmvc.servlet.RequestMappingInfo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class AnnotationHandlerMapping implements HandlerMapping, InstantiationAwareBeanPostProcessor {
    public static Map<String, RequestMappingInfo> map = new HashMap<>();

    @Override
    public RequestMappingInfo getHandlerMapping(String requestURL) {
        return map.get(requestURL);
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        Class<?> aClass = bean.getClass();
        String value = null;
        if (aClass.isAnnotationPresent(Controller.class)) {
            if(aClass.isAnnotationPresent(RequestMapping.class)){
                value = aClass.getDeclaredAnnotation(RequestMapping.class).value();
            }
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                RequestMappingInfo requestMappingInfo = createRequestMappingInfo(value,method, bean);
                if (requestMappingInfo != null) {
                    map.put(requestMappingInfo.getUrl(), requestMappingInfo);
                }
            }
        }
        return true;
    }

    private RequestMappingInfo createRequestMappingInfo(String value , Method method, Object bean) {
        RequestMappingInfo requestMappingInfo = new RequestMappingInfo();
        if (method.isAnnotationPresent(RequestMapping.class)) {
            requestMappingInfo.setMethod(method);
            requestMappingInfo.setUrl(value + method.getDeclaredAnnotation(RequestMapping.class).value());
            requestMappingInfo.setObj(bean);
            return requestMappingInfo;
        }
        return null;
    }
}
