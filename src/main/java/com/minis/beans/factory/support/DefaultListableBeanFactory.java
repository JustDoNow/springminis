package com.minis.beans.factory.support;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.minis.beans.factory.AbstractAutowireCapableBeanFactory;
import com.minis.beans.BeansException;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * DefaultListableBeanFactory
 *
 * @author qizhi
 * @date 2023/06/02
 */
public class  DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
		implements ConfigurableListableBeanFactory {


	ConfigurableListableBeanFactory parentBeanFactory;

	public void setParent(ConfigurableListableBeanFactory beanFactory) {
		this.parentBeanFactory = beanFactory;
	}

	public int getBeanDefinitionCount() {
		return this.beanDefinitionMap.size();
	}

	@Override
	public String[] getBeanDefinitionNames() {
		System.out.println("this.beanDefinitionNames:" + this.beanDefinitionNames);
		return (String[])this.beanDefinitionNames.toArray(new String[this.beanDefinitionNames.size()]);
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

	/**
	 * 当调用 getBean() 获取 Bean 时，先从 WebApplicationContext 中获取，若为空则通过 parentApplicationContext 获取
	 *
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	@Override
	public Object getBean(String beanName) throws BeansException{
		Object result = super.getBean(beanName);
		System.out.println("getBean: " + beanName + ", this:" + this + ", result:" + result);
		if (result == null) {
			result = this.parentBeanFactory.getBean(beanName);
			System.out.println("getBean: " + beanName + "this.parentBeanFactory:" + this.parentBeanFactory
					+ ", result:" + result);
		}

		return result;
	}
}

