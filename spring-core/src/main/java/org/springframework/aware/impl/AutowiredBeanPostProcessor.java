package org.springframework.aware.impl;

import org.springframework.annotation.Autowired;
import org.springframework.annotation.Component;
import org.springframework.aware.BeanPostProcessor;

import java.lang.reflect.Field;

@Component
public class AutowiredBeanPostProcessor implements BeanPostProcessor {
    @Override
    public void autowired(Object instance) {

//        Field[] declaredFields = beanClass.getDeclaredFields();
//        for (Field declaredField : declaredFields) {
//            if(declaredField.isAnnotationPresent(Autowired.class)){
//                //byName
//                declaredField.setAccessible(true);
//                Object bean = getBean(declaredField.getName());
//                try {
//                    declaredField.set(instance,bean);
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//                //byType
//            }
//        }
    }
}
