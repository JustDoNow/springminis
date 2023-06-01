package com.minis.beans;

/**
 * 一个存放 BeanDefinition 的仓库，可以存放、移除、获取及判断 BeanDefinition 对象。
 * 所以，我们初步定义四个接口对应这四个功能，分别是 register、remove、get、contains。
 *
 * @author qizhi
 * @date 2023/06/01
 */
public interface BeanDefinitionRegistry {
	void registerBeanDefinition(String name, BeanDefinition bd);
	void removeBeanDefinition(String name);
	BeanDefinition getBeanDefinition(String name);
	boolean containsBeanDefinition(String name);
}