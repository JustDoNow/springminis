package com.minis.beans;

import com.minis.core.ArgumentValues;
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
	private String[] dependsOn;
	private ArgumentValues constructorArgumentValues;
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
}