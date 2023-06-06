package com.minis.beans.factory.annotation;

import com.minis.beans.factory.BeanFactory;
import com.minis.beans.BeansException;

/**
 * BeanPostProcessor
 *
 * @author qizhi
 * @date 2023/06/02
 */
public interface BeanPostProcessor {
	Object postProcessBeforeInitialization(Object bean, String beanName) throws
			BeansException;

	Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

	void setBeanFactory(BeanFactory beanFactory);
}