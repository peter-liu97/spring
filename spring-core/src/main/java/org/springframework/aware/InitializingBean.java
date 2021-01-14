package org.springframework.aware;

public interface InitializingBean {
    /**
     * 检查bean中的某些重要属性是否符合规则
     */
    void afterPropertiesSet();

}
