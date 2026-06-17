package com.aihoo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


/**
 * <p>
 *
 * </p>
 *
 * @author wyz
 * @since 2026/2/27 14:08
 */
@Component
public class BeanRegistry {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 按名称和类型获取 Bean
     */
    public <T> T getBean(String beanName, Class<T> beanClass) {
        if (!applicationContext.containsBean(beanName)) {
            return null;
        }
        return applicationContext.getBean(beanName, beanClass);
    }
}
