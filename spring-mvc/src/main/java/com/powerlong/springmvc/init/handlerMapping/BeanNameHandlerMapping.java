package com.powerlong.springmvc.init.handlerMapping;

import com.powerlong.springmvc.controller.Controller;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BeanNameHandlerMapping implements HandlerMapping , InstantiationAwareBeanPostProcessor {

    public static Map<String , Controller> map = new HashMap<>();

    @Override
    public Object getHandlerMapping(String requestURL) {
        return map.get(requestURL);
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if(beanName.startsWith("/")){
            map.put(beanName,(Controller)bean);
        }
        return true;
    }
}
