package com.minis.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.minis.exceptions.BeansException;

/**
 * 我们让 SimpleBeanFactory 实现了 BeanDefinitionRegistry，这样 SimpleBeanFactory 既是一个工厂同时也是一个仓库。
 *
 * @author qizhi
 * @date 2023/06/01
 */
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry{

	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
	private List<String> beanDefinitionNames = new ArrayList<>();

	public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
		this.beanDefinitionMap.put(name, beanDefinition);
		this.beanDefinitionNames.add(name);
		if (!beanDefinition.isLazyInit()) {
			try {
				getBean(name);
			} catch (BeansException e) {
			}
		}
	}

	//getBean，容器的核心方法
	public Object getBean(String beanName) throws BeansException {
		//先尝试直接拿bean实例
		Object singleton = this.getSingleton(beanName);
		//如果此时还没有这个bean的实例，则获取它的定义来创建实例
		if (singleton == null) {
			//获取bean的定义
			BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
			if (beanDefinition == null) {
				throw new BeansException("No bean.");
			}
			try {
				singleton = Class.forName(beanDefinition.getClassName()).newInstance();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			//新注册这个bean实例
			this.registerSingleton(beanName, singleton);
		}
		return singleton;
	}
	public void registerBeanDefinition(BeanDefinition beanDefinition) {
		this.beanDefinitionMap.put(beanDefinition.getId(), beanDefinition);
	}

	@Override
	public boolean containsBean(String name) {
		return containsSingleton(name);
	}

	@Override
	public void registerBean(String beanName, Object obj) {
		this.registerSingleton(beanName, obj);
	}

	public void removeBeanDefinition(String name) {
		this.beanDefinitionMap.remove(name);
		this.beanDefinitionNames.remove(name);
		this.removeSingleton(name);
	}
	public BeanDefinition getBeanDefinition(String name) {
		return this.beanDefinitionMap.get(name);
	}
	public boolean containsBeanDefinition(String name) {
		return this.beanDefinitionMap.containsKey(name);
	}
	public boolean isSingleton(String name) {
		return this.beanDefinitionMap.get(name).isSingleton();
	}
	public boolean isPrototype(String name) {
		return this.beanDefinitionMap.get(name).isPrototype();
	}
	public Class<?> getType(String name) {
		return this.beanDefinitionMap.get(name).getClass();
	}
}
