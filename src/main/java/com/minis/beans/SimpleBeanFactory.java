package com.minis.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.minis.BeanDefinition;
import com.minis.exceptions.BeansException;

/**
 * 由 SimpleBeanFactory 的实现不难看出，
 * 这就是把 ClassPathXmlApplicationContext 中有关 BeanDefinition 实例化以及加载到内存中的相关内容提取出来了。
 *
 * @author qizhi
 * @date 2023/06/01
 */
public class SimpleBeanFactory implements BeanFactory{
	private List<BeanDefinition> beanDefinitions = new ArrayList<>();
	private List<String> beanNames = new ArrayList<>();
	private Map<String, Object> singletons = new HashMap<>();
	public SimpleBeanFactory() {
	}

	//getBean，容器的核心方法
	public Object getBean(String beanName) throws BeansException {
		//先尝试直接拿Bean实例
		Object singleton = singletons.get(beanName);
		//如果此时还没有这个Bean的实例，则获取它的定义来创建实例
		if (singleton == null) {
			int i = beanNames.indexOf(beanName);
			if (i == -1) {
				throw new BeansException();
			}
			else {
				//获取Bean的定义
				BeanDefinition beanDefinition = beanDefinitions.get(i);
				try {
					singleton = Class.forName(beanDefinition.getClassName()).newInstance();
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
				//注册Bean实例
				singletons.put(beanDefinition.getId(), singleton);
			}
		}
		return singleton;
	}

	public void registerBeanDefinition(BeanDefinition beanDefinition) {
		this.beanDefinitions.add(beanDefinition);
		this.beanNames.add(beanDefinition.getId());
	}
}
