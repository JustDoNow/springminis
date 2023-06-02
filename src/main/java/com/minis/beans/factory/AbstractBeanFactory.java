package com.minis.beans.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.ConstructorArgumentValue;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import com.minis.beans.factory.support.BeanDefinitionRegistry;
import com.minis.beans.factory.support.DefaultSingletonBeanRegistry;
import com.minis.core.PropertyValue;
import com.minis.core.PropertyValues;
import com.minis.exceptions.BeansException;

/**
 * <p>
 *     1. 启动 ClassPathXmlApplicationContext 容器，执行 refresh()。
 *     2. 在 refresh 执行过程中，调用 registerBeanPostProcessors()，往 BeanFactory 里注册 Bean 处理器，如 AutowiredAnnotationBeanPostProcessor。
 *     3. 执行 onRefresh()， 执行 AbstractBeanFactory 的 refresh() 方法。
 *     4. AbstractBeanFactory 的 refresh() 获取所有 Bean 的定义，执行 getBean() 创建 Bean 实例。
 *     5. getBean() 创建完 Bean 实例后，调用 Bean 处理器并初始化。
 *     6. applyBeanPostProcessorBeforeInitialization 由具体的 BeanFactory，
 *     如 AutowireCapableBeanFactory，来实现，这个实现也很简单，就是对 BeanFactory 里已经注册好的所有 Bean 处理器调用相关方法。
 *     7. 我们事先准备好的 AutowiredAnnotationBeanPostProcessor 方法里面会解释 Bean 中的 Autowired 注解。
 * </p>
 *
 * @author qizhi
 * @date 2023/06/02
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry
		implements BeanFactory, BeanDefinitionRegistry {
	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
	private List<String> beanDefinitionNames = new ArrayList<>();
	private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);
	public AbstractBeanFactory() {
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
	@Override
	public Object getBean(String beanName) throws BeansException {
		//先尝试直接从容器中获取bean实例
		Object singleton = this.getSingleton(beanName);
		if (singleton == null) {
			//如果没有实例，则尝试从毛胚实例中获取
			singleton = this.earlySingletonObjects.get(beanName);
			if (singleton == null) {
				//如果连毛胚都没有，则创建bean实例并注册
				System.out.println("get bean null -------------- " + beanName);
				BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
				singleton = createBean(beanDefinition);
				this.registerBean(beanName, singleton);
				// 进行beanPostProcessor处理
				// step 1: postProcessBeforeInitialization
				applyBeanPostProcessorBeforeInitialization(singleton, beanName);
				// step 2: init-method
				if (beanDefinition.getInitMethodName() != null && !beanDefinition.getInitMethodName().equals("")) {
					invokeInitMethod(beanDefinition, singleton);
				}
				// step 3: postProcessAfterInitialization
				applyBeanPostProcessorAfterInitialization(singleton, beanName);
			}
		}

		return singleton;
	}
	private void invokeInitMethod(BeanDefinition beanDefinition, Object obj) {
		Class<?> clz = beanDefinition.getClass();
		Method method = null;
		try {
			method = clz.getMethod(beanDefinition.getInitMethodName());
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		try {
			method.invoke(obj);
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public boolean containsBean(String name) {
		return containsSingleton(name);
	}
	public void registerBean(String beanName, Object obj) {
		this.registerSingleton(beanName, obj);
	}
	@Override
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

	@Override
	public void removeBeanDefinition(String name) {
		this.beanDefinitionMap.remove(name);
		this.beanDefinitionNames.remove(name);
		this.removeSingleton(name);
	}
	@Override
	public BeanDefinition getBeanDefinition(String name) {
		return this.beanDefinitionMap.get(name);
	}
	@Override
	public boolean containsBeanDefinition(String name) {
		return this.beanDefinitionMap.containsKey(name);
	}
	@Override
	public boolean isSingleton(String name) {
		return this.beanDefinitionMap.get(name).isSingleton();
	}
	@Override
	public boolean isPrototype(String name) {
		return this.beanDefinitionMap.get(name).isPrototype();
	}
	@Override
	public Class<?> getType(String name) {
		return this.beanDefinitionMap.get(name).getClass();
	}
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
		//完善bean，主要是处理属性
		populateBean(beanDefinition, clz, obj);
		return obj;
	}
	//doCreateBean创建毛胚实例，仅仅调用构造方法，没有进行属性处理
	private Object doCreateBean(BeanDefinition beanDefinition) {
		Class<?> clz = null;
		Object obj = null;
		Constructor<?> con = null;
		try {
//			System.out.println("beanDefinition:" + beanDefinition);
			clz = Class.forName(beanDefinition.getClassName());
			// handle constructor
			ConstructorArgumentValues constructorArgumentValues =
					beanDefinition.getConstructorArgumentValues();
			if (!constructorArgumentValues.isEmpty()) {
				Class<?>[] paramTypes = new Class<?>
						[constructorArgumentValues.getArgumentCount()];
				Object[] paramValues = new
						Object[constructorArgumentValues.getArgumentCount()];
				for (int i = 0; i <
						constructorArgumentValues.getArgumentCount(); i++) {
					ConstructorArgumentValue constructorArgumentValue =
							constructorArgumentValues.getIndexedArgumentValue(i);
					if ("String".equals(constructorArgumentValue.getType()) ||
							"java.lang.String".equals(constructorArgumentValue.getType())) {
						paramTypes[i] = String.class;
						paramValues[i] = constructorArgumentValue.getValue();
					} else if
					("Integer".equals(constructorArgumentValue.getType()) ||
									"java.lang.Integer".equals(constructorArgumentValue.getType())) {
						paramTypes[i] = Integer.class;
						paramValues[i] = Integer.valueOf((String)
								constructorArgumentValue.getValue());
					} else if ("int".equals(constructorArgumentValue.getType()))
					{
						paramTypes[i] = int.class;
						paramValues[i] = Integer.valueOf((String)
								constructorArgumentValue.getValue());
					} else {
						paramTypes[i] = String.class;
						paramValues[i] = constructorArgumentValue.getValue();
					}
				}
				try {
					con = clz.getConstructor(paramTypes);
					obj = con.newInstance(paramValues);
				} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException |
						 InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			} else {
				obj = clz.newInstance();
			}
		} catch (Exception e) {
			throw new RuntimeException("beanDefinition:" + beanDefinition.toString(), e);
		}
		System.out.println(beanDefinition.getId() + " bean created. " +
				beanDefinition.getClassName() + " : " + obj.toString());
		return obj;
	}
	private void populateBean(BeanDefinition beanDefinition, Class<?> clz,
							  Object obj) {
		handleProperties(beanDefinition, clz, obj);
	}
	private void handleProperties(BeanDefinition beanDefinition, Class<?> clz,
								  Object obj) {
		// handle properties
		System.out.println("handle properties for bean : " +
				beanDefinition.getId());
		PropertyValues propertyValues = beanDefinition.getPropertyValues();
		//如果有属性
		if (!propertyValues.isEmpty()) {
			for (int i = 0; i < propertyValues.size(); i++) {
				PropertyValue propertyValue =
						propertyValues.getPropertyValueList().get(i);
				String pType = propertyValue.getType();
				String pName = propertyValue.getName();
				Object pValue = propertyValue.getValue();
				boolean isRef = propertyValue.isRef();
				Class<?>[] paramTypes = new Class<?>[1];
				Object[] paramValues = new Object[1];
				if (!isRef) { //如果不是ref，只是普通属性
					//对每一个属性，分数据类型分别处理
					if ("String".equals(pType) ||
							"java.lang.String".equals(pType)) {
						paramTypes[0] = String.class;
					} else if ("Integer".equals(pType) ||
							"java.lang.Integer".equals(pType)) {
						paramTypes[i] = Integer.class;
					} else if ("int".equals(pType)) {
						paramTypes[i] = int.class;
					} else {
						paramTypes[i] = String.class;
					}
					paramValues[0] = pValue;
				} else {//is ref, create the dependent beans
					try {
						paramTypes[0] = Class.forName(pType);
					} catch (ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
					try {//再次调用getBean创建ref的bean实例
						paramValues[0] = getBean((String) pValue);
					} catch (BeansException e) {
						throw new RuntimeException(e);
					}
				}
				//按照setXxxx规范查找setter方法，调用setter方法设置属性
				String methodName = "set" + pName.substring(0, 1).toUpperCase()
						+ pName.substring(1);
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
	abstract public Object applyBeanPostProcessorBeforeInitialization(Object existingBean, String beanName)
			throws BeansException;

	abstract public Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName)
			throws BeansException;
}