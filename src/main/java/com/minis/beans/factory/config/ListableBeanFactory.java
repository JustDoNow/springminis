package com.minis.beans.factory.config;

import java.util.Map;

import com.minis.beans.factory.BeanFactory;
import com.minis.beans.BeansException;

/**
 * ListableBeanFactory
 *
 * @author qizhi
 * @date 2023/06/02
 */
public interface ListableBeanFactory extends BeanFactory {
	boolean containsBeanDefinition(String beanName);
	int getBeanDefinitionCount();
	String[] getBeanDefinitionNames();
	String[] getBeanNamesForType(Class<?> type);
	<T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;
}