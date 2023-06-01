package com.minis.beans;

/**
 * SingletonBeanRegistry
 *
 * @author qizhi
 * @date 2023/06/01
 */
public interface SingletonBeanRegistry {
	void registerSingleton(String beanName, Object singletonObject);
	Object getSingleton(String beanName);
	boolean containsSingleton(String beanName);
	String[] getSingletonNames();
}