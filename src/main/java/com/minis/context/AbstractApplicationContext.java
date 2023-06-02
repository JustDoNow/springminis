package com.minis.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.minis.beans.factory.annotation.BeanPostProcessor;
import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.core.env.Environment;
import com.minis.core.event.ApplicationEventPublisher;
import com.minis.exceptions.BeansException;

/**
 * AbstractApplicationContext
 *
 * @author qizhi
 * @date 2023/06/02
 */

public abstract class AbstractApplicationContext implements ApplicationContext{
	private Environment environment;
	private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new
			ArrayList<>();
	private long startupDate;
	private final AtomicBoolean active = new AtomicBoolean();
	private final AtomicBoolean closed = new AtomicBoolean();
	private ApplicationEventPublisher applicationEventPublisher;
	@Override
	public Object getBean(String beanName) throws BeansException {
		return getBeanFactory().getBean(beanName);
	}

	@Override
	public boolean containsBean(String name) {
		return getBeanFactory().containsBean(name);
	}

//	public void registerBean(String beanName, Object obj) {
//		getBeanFactory().registerBean(beanName, obj);
//	}

	@Override
	public boolean isSingleton(String name) {
		return getBeanFactory().isSingleton(name);
	}

	@Override
	public boolean isPrototype(String name) {
		return getBeanFactory().isPrototype(name);
	}

	@Override
	public Class<?> getType(String name) {
		return getBeanFactory().getType(name);
	}

	public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
		return this.beanFactoryPostProcessors;
	}

	/**
	 * 看名字就比较容易理解，首先是注册监听者，接下来初始化事件发布者，
	 * 随后处理 Bean 以及对 Bean 的状态进行一些操作，
	 * 最后是将初始化完毕的 Bean 进行应用上下文刷新以及完成刷新后进行自定义操作。
	 * 因为这些方法都有 abstract 修饰，允许把这些步骤交给用户自定义处理，因此极大地增强了扩展性。
	 *
	 * @throws BeansException
	 * @throws IllegalStateException
	 */
	public void refresh() throws BeansException, IllegalStateException {
		postProcessBeanFactory(getBeanFactory());
		registerBeanPostProcessors(getBeanFactory());
		initApplicationEventPublisher();
		onRefresh();
		registerListeners();
		finishRefresh();
	}
	abstract void registerListeners();
	abstract void initApplicationEventPublisher();
	abstract void postProcessBeanFactory(ConfigurableListableBeanFactory
												 beanFactory);
	abstract void registerBeanPostProcessors(ConfigurableListableBeanFactory
													 beanFactory);
	abstract void onRefresh();
	abstract void finishRefresh();

	@Override
	public void registerSingleton(String beanName, Object singletonObject) {
		getBeanFactory().registerSingleton(beanName, singletonObject);
	}

	@Override
	public Object getSingleton(String beanName) {
		return getBeanFactory().getSingleton(beanName);
	}

	@Override
	public boolean containsSingleton(String beanName) {
		return getBeanFactory().containsSingleton(beanName);
	}

	@Override
	public String[] getSingletonNames() {
		return getBeanFactory().getSingletonNames();
	}

	@Override
	public String getApplicationName() {
		return "";
	}
	@Override
	public long getStartupDate() {
		return this.startupDate;
	}
	@Override
	public abstract ConfigurableListableBeanFactory getBeanFactory() throws
			IllegalStateException;
	@Override
	public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor
													postProcessor) {
		this.beanFactoryPostProcessors.add(postProcessor);
	}

	@Override
	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
		getBeanFactory().addBeanPostProcessor(beanPostProcessor);

	}

	@Override
	public int getBeanPostProcessorCount() {
		return getBeanFactory().getBeanPostProcessorCount();
	}

	@Override
	public void registerDependentBean(String beanName, String dependentBeanName) {
		getBeanFactory().registerDependentBean(beanName, dependentBeanName);
	}

	@Override
	public String[] getDependentBeans(String beanName) {
		return getBeanFactory().getDependentBeans(beanName);
	}

	@Override
	public String[] getDependenciesForBean(String beanName) {
		return getBeanFactory().getDependenciesForBean(beanName);
	}

	@Override
	public void close() {
	}
	@Override
	public boolean isActive(){
		return true;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public Environment getEnvironment() {
		return this.environment;
	}

	@Override
	public boolean containsBeanDefinition(String beanName) {
		return getBeanFactory().containsBeanDefinition(beanName);
	}

	@Override
	public int getBeanDefinitionCount() {
		return getBeanFactory().getBeanDefinitionCount();
	}

	@Override
	public String[] getBeanDefinitionNames() {
		return getBeanFactory().getBeanDefinitionNames();
	}

	@Override
	public String[] getBeanNamesForType(Class<?> type) {
		return getBeanFactory().getBeanNamesForType(type);
	}

	@Override
	public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
		return getBeanFactory().getBeansOfType(type);
	}

	//省略包装beanfactory的方法

	public ApplicationEventPublisher getApplicationEventPublisher() {
		return applicationEventPublisher;
	}

	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
}