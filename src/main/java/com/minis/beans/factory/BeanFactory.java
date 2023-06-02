package com.minis.beans.factory;

import com.minis.exceptions.BeansException;

/**
 * BeanFactory
 *
 * @author qizhi
 * @date 2023/06/01
 */
public interface BeanFactory {
	Object getBean(String beanName) throws BeansException;
	boolean containsBean(String name);
	boolean isSingleton(String name);
	boolean isPrototype(String name);
	Class<?> getType(String name);
	void registerBean(String beanName, Object obj);
}