package com.minis.core;

import org.dom4j.Element;

import com.minis.BeanDefinition;
import com.minis.beans.BeanFactory;

/**
 * 在 XmlBeanDefinitionReader 中，有一个 loadBeanDefinitions 方法会把解析的 XML 内容转换成 BeanDefinition，
 * 并加载到 BeanFactory 中。
 *
 * @author qizhi
 * @date 2023/06/01
 */
public class XmlBeanDefinitionReader {
	BeanFactory beanFactory;
	public XmlBeanDefinitionReader(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
	public void loadBeanDefinitions(Resource resource) {
		while (resource.hasNext()) {
			Element element = (Element) resource.next();
			String beanID = element.attributeValue("id");
			String beanClassName = element.attributeValue("class");
			BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);
			this.beanFactory.registerBeanDefinition(beanDefinition);
		}
	}
}
