package com.minis.context;

import com.minis.beans.BeanFactory;
import com.minis.beans.SimpleBeanFactory;
import com.minis.core.ApplicationEvent;
import com.minis.core.ApplicationEventPublisher;
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
public class ClassPathXmlApplicationContext implements BeanFactory, ApplicationEventPublisher {
	SimpleBeanFactory beanFactory;

	public ClassPathXmlApplicationContext(String fileName) {
		this(fileName, true);
	}

	public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
		Resource resource = new ClassPathXmlResource(fileName);
		SimpleBeanFactory simpleBeanFactory = new SimpleBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(simpleBeanFactory);
		reader.loadBeanDefinitions(resource);
//		simpleBeanFactory.loadAllNonLazyBeans();

		this.beanFactory = simpleBeanFactory;
		if (isRefresh) {
			this.beanFactory.refresh();
		}
	}

	//context再对外提供一个getBean，底下就是调用的BeanFactory对应的方法
	public Object getBean(String beanName) throws BeansException {
		return this.beanFactory.getBean(beanName);
	}

	@Override
	public boolean containsBean(String name) {
		return this.beanFactory.containsBean(name);
	}

	@Override
	public boolean isSingleton(String name) {
		return this.beanFactory.isSingleton(name);
	}

	@Override
	public boolean isPrototype(String name) {
		return this.beanFactory.isPrototype(name);
	}

	@Override
	public Class<?> getType(String name) {
		return this.beanFactory.getType(name);
	}

	@Override
	public void registerBean(String beanName, Object obj) {
		this.beanFactory.registerBean(beanName, obj);
	}

	@Override
	public void publishEvent(ApplicationEvent event) {

	}
}

