package com.minis.beans.factory.annotation;

import com.minis.exceptions.BeansException;

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
}