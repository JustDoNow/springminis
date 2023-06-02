package com.minis.beans.factory.xml;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.minis.beans.factory.AbstractBeanFactory;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.ConstructorArgumentValue;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import com.minis.beans.factory.support.SimpleBeanFactory;
import com.minis.core.PropertyValue;
import com.minis.core.PropertyValues;
import com.minis.core.Resource;

/**
 * 在 XmlBeanDefinitionReader 中，有一个 loadBeanDefinitions 方法会把解析的 XML 内容转换成 BeanDefinition，
 * 并加载到 BeanFactory 中。
 *
 * @author qizhi
 * @date 2023/06/01
 */
public class XmlBeanDefinitionReader {
	AbstractBeanFactory beanFactory;

	SimpleBeanFactory simpleBeanFactory;

	public XmlBeanDefinitionReader(AbstractBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public XmlBeanDefinitionReader(SimpleBeanFactory beanFactory) {
		this.simpleBeanFactory = beanFactory;
	}

	public void loadBeanDefinitionsV1(Resource resource) {
		while (resource.hasNext()) {
			Element element = (Element) resource.next();
			String beanID = element.attributeValue("id");
			String beanClassName = element.attributeValue("class");
			BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);
			this.beanFactory.registerBeanDefinition(beanID, beanDefinition);
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
					ConstructorArgumentValues AVS = new ConstructorArgumentValues();
			for (Element e : constructorElements) {
				String aType = e.attributeValue("type");
				String aName = e.attributeValue("name");
				String aValue = e.attributeValue("value");
				AVS.addArgumentValue(new ConstructorArgumentValue(aType, aName, aValue));
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

			this.beanFactory.registerBeanDefinition(beanID, beanDefinition);
		}
	}
}