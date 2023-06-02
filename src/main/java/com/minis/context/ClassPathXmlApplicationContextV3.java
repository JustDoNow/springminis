package com.minis.context;

import java.util.List;

import com.minis.beans.factory.AbstractAutowireCapableBeanFactory;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.config.DefaultListableBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;
import com.minis.core.event.ApplicationEvent;
import com.minis.core.event.ApplicationEventPublisher;
import com.minis.core.event.ApplicationListener;
import com.minis.exceptions.BeansException;

/**
 * 支持注解的解析
 *
 * @author qizhi
 * @date 2023/06/02
 */
public class ClassPathXmlApplicationContextV3 implements BeanFactory, ApplicationEventPublisher {

	DefaultListableBeanFactory beanFactory;

	List<BeanFactoryPostProcessor> beanFactoryPostProcessors;

	public ClassPathXmlApplicationContextV3(String fileName, boolean isRefresh) {
		Resource resource = new ClassPathXmlResource(fileName);
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
		reader.loadBeanDefinitions(resource);
		this.beanFactory = beanFactory;
		if (isRefresh) {
			try {
				refresh();
			} catch (BeansException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
		return this.beanFactoryPostProcessors;
	}
	public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor
													postProcessor) {
		this.beanFactoryPostProcessors.add(postProcessor);
	}
	public void refresh() throws BeansException, IllegalStateException {
		// Register bean processors that intercept bean creation.
		registerBeanPostProcessors(this.beanFactory);
		// Initialize other special beans in specific context subclasses.
		onRefresh();
	}
	private void registerBeanPostProcessors(AbstractAutowireCapableBeanFactory beanFactory) {
		beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
	}
	private void onRefresh() {
		this.beanFactory.refresh();
	}

	@Override
	public Object getBean(String beanName) throws BeansException {
		return this.beanFactory.getBean(beanName);
	}

	@Override
	public boolean containsBean(String name) {
		return false;
	}

	@Override
	public boolean isSingleton(String name) {
		return false;
	}

	@Override
	public boolean isPrototype(String name) {
		return false;
	}

	@Override
	public Class<?> getType(String name) {
		return null;
	}

	@Override
	public void publishEvent(ApplicationEvent event) {

	}

	@Override
	public void addApplicationListener(ApplicationListener listener) {

	}
}
