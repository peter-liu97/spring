package org.springframework.context.imp;

import org.springframework.annotation.*;
import org.springframework.aware.BeanNameAware;
import org.springframework.aware.BeanPostProcessor;
import org.springframework.aware.InitializingBean;
import org.springframework.beandinition.BeanDefinition;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liushimin
 */

public class AnnotationConfigApplicationContext implements ApplicationContext {

    private Class<?> configClass;

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

    private Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>();

    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public AnnotationConfigApplicationContext() {
    }

    public AnnotationConfigApplicationContext(Class<?> configClass) {
        this.configClass = configClass;

        /**
         * 扫描
         */
        scanPath(configClass);

        /**
         * 创建非懒加载的bean
         */
        creatBean();
    }

    private void creatBean() {
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if ("singleton".equals(beanDefinition.getScope()) && !beanDefinition.isLazy()) {
                Object bean = doCreatBean(beanDefinition,beanName);
                singletonObjects.put(beanName, bean);
            }
        }
    }

    private Object doCreatBean(BeanDefinition beanDefinition ,String beanName) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        /**
         * 推断构造方法
         */
        Object instance = null;
        try {
            instance = beanClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        /**
         * 属性注入
         */
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if(declaredField.isAnnotationPresent(Autowired.class)){
                //byName
                declaredField.setAccessible(true);
                Object bean = getBean(declaredField.getName());
                try {
                    declaredField.set(instance,bean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                //byType
            }
        }

        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            beanPostProcessor.autowired(instance);
        }

        if (instance instanceof BeanNameAware) {
            ((BeanNameAware)instance).setBeanName(beanName);
        }

        if (instance instanceof InitializingBean) {
            ((InitializingBean)instance).afterPropertiesSet();
        }



        return instance;
    }

    private void scanPath(Class<?> configClass) {

        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = configClass.getAnnotation(ComponentScan.class);
            try {
                doScan(componentScan.value());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    public Object getBean(String beanName) {
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new NullPointerException();
        }
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        String scope = beanDefinition.getScope();
        if ("singleton".equals(scope)) {
            //单例池中获取
            Object o = singletonObjects.get(beanName);
            if (o == null){
               o = doCreatBean(beanDefinition,beanName);
                singletonObjects.put(beanName,o);
            }
            return o;
        } else if ("prototype".equals(scope)) {
            //创建一个bean
            return doCreatBean(beanDefinition,beanName);
        }
        return null;
    }

    public void doScan(String path) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        String replace = path.replace(".", "/");
        ClassLoader classLoader = AnnotationConfigApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(replace);
        File file = new File(resource.getFile());
        for (File f : file.listFiles()) {
            String s = f.getAbsolutePath();
            if (s.endsWith(".class")) {
                s = s.substring(s.indexOf("com"), s.indexOf(".class"));
                s = s.replace("\\", ".");
            }
            Class<?> aClass = classLoader.loadClass(s);
            if (aClass.isAnnotationPresent(Component.class)) {
                if(BeanPostProcessor.class.isAssignableFrom(aClass)){
                    BeanPostProcessor o = (BeanPostProcessor)aClass.getDeclaredConstructor().newInstance();
                    beanPostProcessorList.add(o);
                }

                BeanDefinition beanDefinition = new BeanDefinition();
                beanDefinition.setBeanClass(aClass);
                Component component = aClass.getAnnotation(Component.class);
                String beanName = component.value();
                beanDefinition.setBeanName(beanName);
                if (aClass.isAnnotationPresent(Lazy.class)) {
                    beanDefinition.setLazy(true);
                }
                if (aClass.isAnnotationPresent(Scope.class)) {
                    Scope annotation = aClass.getAnnotation(Scope.class);
                    String value = annotation.value();
                    beanDefinition.setScope(value);
                } else {
                    beanDefinition.setScope("singleton");
                }

                beanDefinitionMap.put(beanName,beanDefinition);

            }

        }
    }


}
