package com.minis.beans.factory.config;

import com.minis.beans.factory.BeanFactory;
import com.minis.beans.BeansException;

/**
 * BeanFactoryPostProcessor
 *
 * @author qizhi
 * @date 2023/06/02
 */
public interface BeanFactoryPostProcessor {
	void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException;
}
