package org.springframework.aware;

public interface BeanNameAware extends Aware{

    void setBeanName(String name);
}
