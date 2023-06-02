package com.minis.beans.factory.config;

import com.minis.beans.factory.BeanFactory;
import com.minis.exceptions.BeansException;

/**
 * AutowireCapableBeanFactory
 *
 * @author qizhi
 * @date 2023/06/02
 */
public interface AutowireCapableBeanFactory  extends BeanFactory {
	int AUTOWIRE_NO = 0;
	int AUTOWIRE_BY_NAME = 1;
	int AUTOWIRE_BY_TYPE = 2;
	Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;
	Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;
}