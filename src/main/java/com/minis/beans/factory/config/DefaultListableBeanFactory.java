package com.minis.beans.factory.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.minis.beans.factory.AbstractAutowireCapableBeanFactory;
import com.minis.exceptions.BeansException;

/**
 * DefaultListableBeanFactory
 *
 * @author qizhi
 * @date 2023/06/02
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
		implements ConfigurableListableBeanFactory{
	public int getBeanDefinitionCount() {
		return this.beanDefinitionMap.size();
	}
	public String[] getBeanDefinitionNames() {
		return (String[]) this.beanDefinitionNames.toArray();
	}
	public String[] getBeanNamesForType(Class<?> type) {
		List<String> result = new ArrayList<>();
		for (String beanName : this.beanDefinitionNames) {
			boolean matchFound = false;
			BeanDefinition mbd = this.getBeanDefinition(beanName);

			//FIXME qizhi 这里获取Class有问题
			Class<?> classToMatch = mbd.getClass();
			if (type.isAssignableFrom(classToMatch)) {
				matchFound = true;
			}
			else {
				matchFound = false;
			}
			if (matchFound) {
				result.add(beanName);
			}
		}
		return (String[]) result.toArray();
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException
	{
		String[] beanNames = getBeanNamesForType(type);
		Map<String, T> result = new LinkedHashMap<>(beanNames.length);
		for (String beanName : beanNames) {
			Object beanInstance = getBean(beanName);
			result.put(beanName, (T) beanInstance);
		}
		return result;
	}
}

