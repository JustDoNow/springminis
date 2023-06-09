package com.minis.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.config.ConstructorArgumentValue;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import com.minis.core.PropertyValue;
import com.minis.core.PropertyValues;
import com.minis.beans.BeansException;

/**
 * 我们让 SimpleBeanFactory 实现了 BeanDefinitionRegistry，这样 SimpleBeanFactory 既是一个工厂同时也是一个仓库。
 *
 * @author qizhi
 * @date 2023/06/01
 */
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {

	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

	// 毛胚实例
	private Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(256);

	private List<String> beanDefinitionNames = new ArrayList<>();

	public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
		this.beanDefinitionMap.put(name, beanDefinition);
		this.beanDefinitionNames.add(name);
	}

	/**
	 * 全部加载完xml，再解析bean，否则前面的bean引用后面的bean时，内存里无法找到bean的定义
	 */
	public void loadAllNonLazyBeans() {
		for (String beanName : beanDefinitionMap.keySet()) {
			if (!beanDefinitionMap.get(beanName).isLazyInit()) {
				try {
					getBean(beanName);
				} catch (BeansException ignored) {

				}
			}
		}
	}

	//getBean，容器的核心方法
	@Override
	public Object getBean(String beanName) throws BeansException {
		//先尝试直接从容器中获取bean实例
		System.out.println(Thread.currentThread().getName() + "-" + System.currentTimeMillis() + "-" + "getBean:" + beanName);
		Object singleton = this.getSingleton(beanName);
		if (singleton == null) {
			//如果没有实例，则尝试从毛胚实例中获取
			singleton = this.earlySingletonObjects.get(beanName);
			if (singleton == null) {
				//如果连毛胚都没有，则创建bean实例并注册
				BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
				singleton = createBean(beanDefinition);
				this.registerSingleton(beanName, singleton);
				// 预留beanpostprocessor位置
				// step 1: postProcessBeforeInitialization
				// step 2: afterPropertiesSet
				// step 3: init-method
				// step 4: postProcessAfterInitialization
			}
		}
		return singleton;
	}

	/**
	 * 首先，获取 XML 配置中的属性值，这个时候它们都是通用的 Object 类型，我们需要根据 type 字段的定义判断不同 Value 所属的类型，
	 * 作为一个原始的实现这里我们只提供了 String、Integer 和 int 三种类型的判断。
	 * 最终通过反射构造对象，将配置的属性值注入到了 Bean 对象中，实现构造器注入。
	 * 和处理 constructor 相同，我们依然要通过 type 字段确定 Value 的归属类型。但不同之处在于，判断好归属类型后，
	 * 我们还要手动构造 setter 方法，通过反射将属性值注入到 setter 方法之中。
	 * 通过这种方式来实现对属性的赋值。
	 * 可以看出，其实代码的核心是通过 Java 的反射机制调用构造器及 setter 方法，在调用过程中根据具体的类型把属性值作为一个参数赋值进去。
	 * 这也是所有的框架在实现 IoC 时的思路。反射技术是 IoC 容器赖以工作的基础。
	 *
	 * @param beanDefinition
	 * @return
	 */
	private Object createBean(BeanDefinition beanDefinition) {
		Class<?> clz = null;
		//创建毛胚bean实例
		Object obj = doCreateBean(beanDefinition);
		//存放到毛胚实例缓存中
		this.earlySingletonObjects.put(beanDefinition.getId(), obj);
		try {
			clz = Class.forName(beanDefinition.getClassName());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		//处理属性
		handleProperties(beanDefinition, clz, obj);
		return obj;
	}

	//doCreateBean创建毛胚实例，仅仅调用构造方法，没有进行属性处理
	private Object doCreateBean(BeanDefinition bd) throws RuntimeException {
		Class<?> clz = null;
		Object obj = null;
		Constructor<?> con = null;

		try {
			clz = Class.forName(bd.getClassName());

			//handle constructor
			ConstructorArgumentValues argumentValues = bd.getConstructorArgumentValues();
			if (!argumentValues.isEmpty()) {
				Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
				Object[] paramValues = new Object[argumentValues.getArgumentCount()];
				for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
					ConstructorArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
					if ("String".equals(argumentValue.getType()) || "java.lang.String".equals(argumentValue.getType())) {
						paramTypes[i] = String.class;
						paramValues[i] = argumentValue.getValue();
					} else if ("Integer".equals(argumentValue.getType()) || "java.lang.Integer".equals(argumentValue.getType())) {
						paramTypes[i] = Integer.class;
						paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
					} else if ("int".equals(argumentValue.getType())) {
						paramTypes[i] = int.class;
						paramValues[i] = Integer.valueOf((String) argumentValue.getValue()).intValue();
					} else {
						paramTypes[i] = String.class;
						paramValues[i] = argumentValue.getValue();
					}
				}
				try {
					System.out.println("paramTypes:" + Arrays.toString(paramTypes) + ",," + "paramValues:" + Arrays.toString(paramValues));
					con = clz.getConstructor(paramTypes);
					obj = con.newInstance(paramValues);
				} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException |
						 InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			} else {
				obj = clz.newInstance();
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		System.out.println(bd.getId() + " bean created. " + bd.getClassName() + " : " + obj.toString());
		return obj;

	}

	private void handleProperties(BeanDefinition bd, Class<?> clz, Object obj) {
		// 处理属性
		System.out.println("handle properties for bean : " + bd.getId());
		PropertyValues propertyValues = bd.getPropertyValues();
		//如果有属性
		if (!propertyValues.isEmpty()) {
			for (int i = 0; i < propertyValues.size(); i++) {
				PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
				String pName = propertyValue.getName();
				String pType = propertyValue.getType();
				Object pValue = propertyValue.getValue();
				boolean isRef = propertyValue.isRef();
				Class<?>[] paramTypes = new Class<?>[1];
				Object[] paramValues = new Object[1];
				if (!isRef) { //如果不是ref，只是普通属性
					//对每一个属性，分数据类型分别处理
					if ("String".equals(pType) || "java.lang.String".equals(pType)) {
						paramTypes[0] = String.class;
					} else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
						paramTypes[0] = Integer.class;
					} else if ("int".equals(pType)) {
						paramTypes[0] = int.class;
					} else {
						paramTypes[0] = String.class;
					}

					paramValues[0] = pValue;
				} else { //is ref, create the dependent beans
					try {
						paramTypes[0] = Class.forName(pType);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					try {
						//再次调用getBean创建ref的bean实例
						paramValues[0] = getBean((String) pValue);
					} catch (BeansException e) {
						throw new RuntimeException(e);
					}
				}

				//按照setXxxx规范查找setter方法，调用setter方法设置属性
				String methodName = "set" + pName.substring(0, 1).toUpperCase() + pName.substring(1);
				Method method = null;
				try {
					method = clz.getMethod(methodName, paramTypes);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e);
				}
				try {
					method.invoke(obj, paramValues);
				} catch (InvocationTargetException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public void registerBeanDefinition(BeanDefinition beanDefinition) {
		this.beanDefinitionMap.put(beanDefinition.getId(), beanDefinition);
	}

	@Override
	public boolean containsBean(String name) {
		return containsSingleton(name);
	}

//	@Override
//	public void registerBean(String beanName, Object obj) {
//		this.registerSingleton(beanName, obj);
//	}

	public void removeBeanDefinition(String name) {
		this.beanDefinitionMap.remove(name);
		this.beanDefinitionNames.remove(name);
		this.removeSingleton(name);
	}

	public void refresh() {
		for (String beanName : beanDefinitionNames) {
			try {
				getBean(beanName);
			} catch (BeansException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public BeanDefinition getBeanDefinition(String name) {
		return this.beanDefinitionMap.get(name);
	}

	public boolean containsBeanDefinition(String name) {
		return this.beanDefinitionMap.containsKey(name);
	}

	public boolean isSingleton(String name) {
		return this.beanDefinitionMap.get(name).isSingleton();
	}

	public boolean isPrototype(String name) {
		return this.beanDefinitionMap.get(name).isPrototype();
	}

	public Class<?> getType(String name) {
		return this.beanDefinitionMap.get(name).getClass();
	}
}
