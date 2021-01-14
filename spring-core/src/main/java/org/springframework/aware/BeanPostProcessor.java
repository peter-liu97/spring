package org.springframework.aware;

public interface BeanPostProcessor {
    void autowired(Object instance);

    default Object postProcessorBeforeInitialization(Object bean , String beanName){
        return bean;
    }

    default Object postProcessorAfterInitialization(Object bean , String beanName){
        return bean;
    }
}
