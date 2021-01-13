package com.springframework.test;

import com.springframework.Application;
import org.springframework.context.imp.AnnotationConfigApplicationContext;

public class Test {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Application.class);

        Object userService = applicationContext.getBean("userService");

        System.out.println(userService);
    }

}
