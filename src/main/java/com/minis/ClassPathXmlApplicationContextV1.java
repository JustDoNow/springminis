package com.minis;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.minis.beans.factory.config.BeanDefinition;

/**
 * ClassPathXmlApplicationContext 定义了唯一的构造函数，构造函数里会做两件事：
 * 一是提供一个 readXml() 方法，通过传入的文件路径，也就是 XML 文件的全路径名，来获取 XML 内的信息，
 * 二是提供一个 instanceBeans() 方法，根据读取到的信息实例化 Bean。
 *
 * @author qizhi
 * @date 2023/06/01
 */
public class ClassPathXmlApplicationContextV1 {
	private List<BeanDefinition> beanDefinitions = new ArrayList<>();
	private Map<String, Object> singletons = new HashMap<>();
	//构造器获取外部配置，解析出Bean的定义，形成内存映像
	public ClassPathXmlApplicationContextV1(String fileName) {
		this.readXml(fileName);
		this.instanceBeans();
	}

	/**
	 * 首先来看 readXML，这也是我们解析 Bean 的核心方法，因为配置在 XML 内的 Bean 信息都是文本信息，需要解析之后变成内存结构才能注入到容器中。
	 * 该方法最开始创建了 SAXReader 对象，这个对象是 dom4j 包内提供的。
	 * 随后，它通过传入的 fileName，也就是定义的 XML 名字，获取根元素，也就是 XML 里最外层的标签。
	 * 然后它循环遍历标签中的属性，通过 element.attributeValue("id") 和 element.attributeValue("class") 拿到配置信息，
	 * 接着用这些配置信息构建 BeanDefinition 对象，然后把 BeanDefinition 对象加入到 BeanDefinitions 列表中，
	 * 这个地方就保存了所有 Bean 的定义。
	 * @param fileName
	 */
	private void readXml(String fileName) {
		SAXReader saxReader = new SAXReader();
		try {
			URL xmlPath =
					this.getClass().getClassLoader().getResource(fileName);
			Document document = saxReader.read(xmlPath);
			Element rootElement = document.getRootElement();
			//对配置文件中的每一个<bean>，进行处理
			for (Element element : (List<Element>) rootElement.elements()) {
				//获取Bean的基本信息
				String beanID = element.attributeValue("id");
				String beanClassName = element.attributeValue("class");
				BeanDefinition beanDefinition = new BeanDefinition(beanID,
						beanClassName);
				//将Bean的定义存放到beanDefinitions
				beanDefinitions.add(beanDefinition);
			}
		} catch (DocumentException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 我们看看 instanceBeans 方法实现的功能：实例化一个 Bean。
	 * 因为 BeanDefinitions 存储的 BeanDefinition 的 class 只是一个类的全名，所以我们现在需要将这个名字转换成一个具体的类。
	 * 我们可以通过 Java 里的反射机制，也就是 Class.forName 将一个类的名字转化成一个实际存在的类，转成这个类之后，
	 * 我们把它放到 singletons 这个 Map 里，构建 ID 与实际类的映射关系。
	 */
	private void instanceBeans() {
		for (BeanDefinition beanDefinition : beanDefinitions) {
			try {
				// 利用反射创建Bean实例，并存储在singletons中
				singletons.put(beanDefinition.getId(),
						Class.forName(beanDefinition.getClassName()).newInstance());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
	//这是对外的一个方法，让外部程序从容器中获取Bean实例，会逐步演化成核心方法
	public Object getBean(String beanName) {
		return singletons.get(beanName);
	}
}
