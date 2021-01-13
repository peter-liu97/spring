package com;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class Test {

    public void test(String str1 , String str2){
        System.out.println(str1 + str2);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> test = Class.forName("com.Test");
        Method[] methods = test.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                System.out.println(parameter.getName());
            }
            System.out.println(name);
        }
        System.out.println();
    }
}
