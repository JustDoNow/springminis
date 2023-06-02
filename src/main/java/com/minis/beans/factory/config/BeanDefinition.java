package com.minis.beans.factory.config;

import java.util.Arrays;

import com.minis.core.PropertyValues;

import lombok.Getter;
import lombok.Setter;

/**
 * BeanDefinition
 *
 * @author qizhi
 * @date 2023/06/01
 */
@Getter
@Setter
public class BeanDefinition {
	String SCOPE_SINGLETON = "singleton";
	String SCOPE_PROTOTYPE = "prototype";
	private boolean lazyInit = false;

	/** 记录了某一个 Bean 引用的其他 Bean */
	private String[] dependsOn;
	private ConstructorArgumentValues constructorArgumentValues;
	private PropertyValues propertyValues;
	private String initMethodName;
	private volatile Object beanClass;
	private String id;
	private String className;
	private String scope = SCOPE_SINGLETON;

	public BeanDefinition(String id, String className) {
		this.id = id;
		this.className = className;
	}

	public boolean isSingleton() {
		return SCOPE_SINGLETON.equals(this.scope);
	}

	public boolean isPrototype() {
		return SCOPE_PROTOTYPE.equals(this.scope);
	}
	//省略getter和setter


	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("BeanDefinition{");
		sb.append("lazyInit=").append(lazyInit);
		sb.append(", dependsOn=").append(Arrays.toString(dependsOn));
		sb.append(", constructorArgumentValues=").append(constructorArgumentValues);
		sb.append(", propertyValues=").append(propertyValues);
		sb.append(", initMethodName='").append(initMethodName).append('\'');
		sb.append(", beanClass=").append(beanClass);
		sb.append(", id='").append(id).append('\'');
		sb.append(", className='").append(className).append('\'');
		sb.append(", scope='").append(scope).append('\'');
		sb.append('}');
		return sb.toString();
	}
}