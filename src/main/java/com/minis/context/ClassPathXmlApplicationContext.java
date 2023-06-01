package com.minis.context;

import com.minis.BeanDefinition;
import com.minis.beans.BeanFactory;
import com.minis.beans.SimpleBeanFactory;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;
import com.minis.core.XmlBeanDefinitionReader;
import com.minis.exceptions.BeansException;

/**
 * context负责整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactory
 *
 * @author qizhi
 * @date 2023/06/01
 */
public class ClassPathXmlApplicationContext implements BeanFactory{
	BeanFactory beanFactory;

	public ClassPathXmlApplicationContext(String fileName) {
		Resource resource = new ClassPathXmlResource(fileName);
		BeanFactory beanFactory = new SimpleBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
		reader.loadBeanDefinitions(resource);
		this.beanFactory = beanFactory;
	}
	//context再对外提供一个getBean，底下就是调用的BeanFactory对应的方法
	public Object getBean(String beanName) throws BeansException {
		return this.beanFactory.getBean(beanName);
	}
	public void registerBeanDefinition(BeanDefinition beanDefinition) {
		this.beanFactory.registerBeanDefinition(beanDefinition);
	}
}
