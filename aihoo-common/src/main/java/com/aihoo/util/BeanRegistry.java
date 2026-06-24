package com.aihoo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanRegistry {

    @Autowired
    private ApplicationContext applicationContext;

    
    public <T> T getBean(String beanName, Class<T> beanClass) {
        if (!applicationContext.containsBean(beanName)) {
            return null;
        }
        return applicationContext.getBean(beanName, beanClass);
    }
}
