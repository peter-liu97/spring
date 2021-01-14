package com.springframework.service;

import org.springframework.annotation.Autowired;
import org.springframework.annotation.Component;
import org.springframework.aware.BeanNameAware;
import org.springframework.aware.InitializingBean;

@Component(value = "userService")
public class UserService implements BeanNameAware, InitializingBean {

    private String beanName;

    @Autowired
    private OrderService orderService;

    public void setBeanName(String name) {
        this.beanName = name;
    }

    public void afterPropertiesSet() {

    }
}
