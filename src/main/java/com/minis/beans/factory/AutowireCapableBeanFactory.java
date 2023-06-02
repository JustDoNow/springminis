package com.minis.beans.factory;

import java.util.ArrayList;
import java.util.List;

import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.annotation.BeanPostProcessor;
import com.minis.exceptions.BeansException;

/**
 * AutowireCapableBeanFactory
 *
 * @author qizhi
 * @date 2023/06/02
 */
public class AutowireCapableBeanFactory extends AbstractBeanFactory{

	// 用一个列表 beanPostProcessors 记录所有的 Bean 处理器，这样可以按照需求注册若干个不同用途的处理器，然后调用处理器。
	private final List<AutowiredAnnotationBeanPostProcessor> beanPostProcessors =
			new ArrayList<>();
	public void addBeanPostProcessor(AutowiredAnnotationBeanPostProcessor
											 beanPostProcessor) {
		this.beanPostProcessors.remove(beanPostProcessor);
		this.beanPostProcessors.add(beanPostProcessor);
	}
	public int getBeanPostProcessorCount() {
		return this.beanPostProcessors.size();
	}
	public List<AutowiredAnnotationBeanPostProcessor> getBeanPostProcessors() {
		return this.beanPostProcessors;
	}

	@Override
	public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName) throws BeansException {
		Object result = existingBean;
		for (AutowiredAnnotationBeanPostProcessor beanProcessor :
				getBeanPostProcessors()) {
			beanProcessor.setBeanFactory(this);
			result = beanProcessor.postProcessBeforeInitialization(result,
					beanName);
			if (result == null) {
				return result;
			}
		}
		return result;
	}

	@Override
	public Object applyBeanPostProcessorAfterInitialization(Object existingBean,
															 String beanName) throws BeansException {
		Object result = existingBean;
		for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
			result = beanProcessor.postProcessAfterInitialization(result,
					beanName);
			if (result == null) {
				return result;
			}
		}
		return result;
	}
}
