package com.powerlong.springmvc;

import com.powerlong.springmvc.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.powerlong.springmvc")
public class Start {
    private static String DOC_BASE = "D:\\spring\\spring-mvc\\src\\main\\webapp";

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        Context appContext = tomcat.addWebapp("/", DOC_BASE);
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Start.class);
        tomcat.addServlet(appContext , "dispatcherServlet" , new DispatcherServlet(ac));
        appContext.addServletMapping("/","dispatcherServlet");
        tomcat.start();
        tomcat.getServer().await();

    }
}
