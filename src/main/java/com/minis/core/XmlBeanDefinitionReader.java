package com.minis.core;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.minis.beans.BeanDefinition;
import com.minis.beans.SimpleBeanFactory;

/**
 * 在 XmlBeanDefinitionReader 中，有一个 loadBeanDefinitions 方法会把解析的 XML 内容转换成 BeanDefinition，
 * 并加载到 BeanFactory 中。
 *
 * @author qizhi
 * @date 2023/06/01
 */
public class XmlBeanDefinitionReader {
	SimpleBeanFactory simpleBeanFactory;

	public XmlBeanDefinitionReader(SimpleBeanFactory simpleBeanFactory) {
		this.simpleBeanFactory = simpleBeanFactory;
	}

	public void loadBeanDefinitionsV1(Resource resource) {
		while (resource.hasNext()) {
			Element element = (Element) resource.next();
			String beanID = element.attributeValue("id");
			String beanClassName = element.attributeValue("class");
			BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);
			this.simpleBeanFactory.registerBeanDefinition(beanDefinition);
		}
	}


	public void loadBeanDefinitions(Resource resource) {
		while (resource.hasNext()) {
			Element element = (Element) resource.next();
			String beanID = element.attributeValue("id");
			String beanClassName = element.attributeValue("class");
			BeanDefinition beanDefinition = new BeanDefinition(beanID,
					beanClassName);
			// handle constructor
			List<Element> constructorElements = element.elements("constructor-arg");
					ArgumentValues AVS = new ArgumentValues();
			for (Element e : constructorElements) {
				String aType = e.attributeValue("type");
				String aName = e.attributeValue("name");
				String aValue = e.attributeValue("value");
				AVS.addArgumentValue(new ArgumentValue(aType, aName, aValue));
			}
			beanDefinition.setConstructorArgumentValues(AVS);

			// handle properties
			List<Element> propertyElements = element.elements("property");
			PropertyValues PVS = new PropertyValues();
			List<String> refs = new ArrayList<>();
			for (Element e : propertyElements) {
				String pType = e.attributeValue("type");
				String pName = e.attributeValue("name");
				String pValue = e.attributeValue("value");
				String pRef = e.attributeValue("ref");
				String pV = "";
				boolean isRef = false;
				if (pValue != null && !pValue.equals("")) {
					isRef = false;
					pV = pValue;
				} else if (pRef != null && !pRef.equals("")) {
					isRef = true;
					pV = pRef;
					refs.add(pRef);
				}
				PVS.addPropertyValue(new PropertyValue(pType, pName, pV, isRef));
			}
			beanDefinition.setPropertyValues(PVS);

			String[] refArray = refs.toArray(new String[0]);
			// setDependsOn 方法，它记录了某一个 Bean 引用的其他 Bean
			beanDefinition.setDependsOn(refArray);

			//FIXME 边加载xml，边解析bean，存在问题：前面的bean引用后面的bean时，内存里无法找到bean的定义
			this.simpleBeanFactory.registerBeanDefinition(beanID, beanDefinition);
		}
	}
}