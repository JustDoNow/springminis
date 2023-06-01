package com.minis.beans;

import com.minis.BeanDefinition;
import com.minis.exceptions.BeansException;

/**
 * BeanFactory
 *
 * @author qizhi
 * @date 2023/06/01
 */
public interface BeanFactory {
	Object getBean(String beanName) throws BeansException;
	void registerBeanDefinition(BeanDefinition beanDefinition);
}