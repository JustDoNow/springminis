package com.minis.beans.factory.annotation;

import java.lang.reflect.Field;

import com.minis.beans.factory.AbstractAutowireCapableBeanFactory;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.BeansException;

/**
 * 判断类里面的每一个属性是不是带有 Autowired 注解，如果有，就根据属性名获取 Bean。
 * 从这里我们可以看出，属性名字很关键，我们就是靠它来获取和创建的 Bean。
 * 有了 Bean 之后，我们通过反射设置属性值，完成依赖注入。
 *
 * @author qizhi
 * @date 2023/06/02
 */
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
	private BeanFactory beanFactory;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		Object result = bean;

		Class<?> clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		if(fields!=null){
			//对每一个属性进行判断，如果带有@Autowired注解则进行处理
			for(Field field : fields){
				boolean isAutowired =
						field.isAnnotationPresent(Autowired.class);
				if(isAutowired){
					//根据属性名查找同名的bean
					String fieldName = field.getName();
					Object autowiredObj =
							this.getBeanFactory().getBean(fieldName);
					//设置属性值，完成注入
					try {
						field.setAccessible(true);
						field.set(bean, autowiredObj);
						System.out.println("autowire " + fieldName + " for bean " + beanName);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return result;
	}
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		return null;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}
	public void setBeanFactory(AbstractAutowireCapableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
}

