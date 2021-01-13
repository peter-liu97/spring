package org.springframework.context.imp;

import org.springframework.annotation.Component;
import org.springframework.annotation.ComponentScan;
import org.springframework.annotation.Lazy;
import org.springframework.annotation.Scope;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;

/**
 * @author liushimin
 */

public class AnnotationConfigApplicationContext implements ApplicationContext {

    private Class configClass;

    public AnnotationConfigApplicationContext() {
    }

    public AnnotationConfigApplicationContext(Class configClass) {
        this.configClass = configClass;

        /**
         * 扫描
         */
        scanPath(configClass);
    }

    private void scanPath(Class configClass) {

        Annotation[] annotations = configClass.getAnnotations();

        if (annotations.length == 0) {

        }

        for (Annotation annotation : annotations) {
            if (annotation instanceof ComponentScan) {
                ComponentScan componentScan = (ComponentScan) annotation;
                try {
                    doScan(componentScan.value());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public Object getBean(String beanName) {

        return null;
    }

    public void doScan(String path) throws ClassNotFoundException {

        String replace = path.replace(".", "/");
        ClassLoader classLoader = AnnotationConfigApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(replace);
        File file = new File(resource.getFile());
        for (File f : file.listFiles()) {
            String s = f.getAbsolutePath();
            if (s.endsWith(".class")) {
                s = s.substring(s.indexOf("com"), s.indexOf(".class"));
                s.replace("\\",".");
            }
            Class<?> aClass = classLoader.loadClass(s);
            if(aClass.isAnnotationPresent(Component.class)){

                if(aClass.isAnnotationPresent(Lazy.class)){

                }

                if(aClass.isAnnotationPresent(Scope.class)){

                }

            }

        }
    }


}
