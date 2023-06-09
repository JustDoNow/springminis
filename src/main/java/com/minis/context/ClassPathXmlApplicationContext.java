package com.minis.context;

import java.util.ArrayList;
import java.util.List;

import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;
import com.minis.beans.BeansException;

/**
 * ClassPathXmlApplicationContext
 *
 * @author qizhi
 * @date 2023/06/02
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext{
	DefaultListableBeanFactory beanFactory;
	private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new
			ArrayList<>();
	public ClassPathXmlApplicationContext(String fileName) {
		this(fileName, true);
	}
	public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
		Resource resource = new ClassPathXmlResource(fileName);
		DefaultListableBeanFactory beanFactory = new
				DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new
				XmlBeanDefinitionReader(beanFactory);
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

	@Override
	public void registerListeners() {
		ApplicationListener listener = new ApplicationListener();
		this.getApplicationEventPublisher().addApplicationListener(listener);
	}
	@Override
	public void initApplicationEventPublisher() {
		ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
		this.setApplicationEventPublisher(aep);
	}
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
	}
	@Override
	public void publishEvent(ApplicationEvent event) {
		this.getApplicationEventPublisher().publishEvent(event);
	}
	@Override
	public void addApplicationListener(ApplicationListener listener) {
		this.getApplicationEventPublisher().addApplicationListener(listener);
	}
	public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor
													postProcessor) {
		this.beanFactoryPostProcessors.add(postProcessor);
	}
	@Override
	public void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory)
	{
		this.beanFactory.addBeanPostProcessor(new
				AutowiredAnnotationBeanPostProcessor());
	}
	@Override
	public void onRefresh() {
		this.beanFactory.refresh();
	}
	@Override
	public ConfigurableListableBeanFactory getBeanFactory() throws
			IllegalStateException {
		return this.beanFactory;
	}
	@Override
	public void finishRefresh() {
		publishEvent(new ContextRefreshEvent("Context Refreshed..."));
	}


}