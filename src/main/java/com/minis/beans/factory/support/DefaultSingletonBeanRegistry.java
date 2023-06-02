package com.minis.beans.factory.support;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.minis.beans.factory.config.SingletonBeanRegistry;

/**
 * DefaultSingletonBeanRegistry
 *
 * @author qizhi
 * @date 2023/06/01
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
	//容器中存放所有bean的名称的列表
	protected List<String> beanNames = new ArrayList<>();
	//容器中存放所有bean实例的map
	protected Map<String, Object> singletonObjects =new ConcurrentHashMap<>(256);
	protected Map<String,Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);
	protected Map<String,Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);

	/**
	 * 我们将 singletons 定义为了一个 ConcurrentHashMap，而且在实现 registrySingleton 时前面加了一个关键字 synchronized。
	 * 这一切都是为了确保在多线程并发的情况下，我们仍然能安全地实现对单例 Bean 的管理，无论是单线程还是多线程，
	 * 我们整个系统里面这个 Bean 总是唯一的、单例的。
	 *
	 * @param beanName
	 * @param singletonObject
	 */
	public void registerSingleton(String beanName, Object singletonObject) {
		synchronized (this.singletonObjects) {
			this.singletonObjects.put(beanName, singletonObject);
			this.beanNames.add(beanName);
		}
	}
	public Object getSingleton(String beanName) {
		return this.singletonObjects.get(beanName);
	}
	public boolean containsSingleton(String beanName) {
		return this.singletonObjects.containsKey(beanName);
	}
	public String[] getSingletonNames() {
		return (String[]) this.beanNames.toArray();
	}
	protected void removeSingleton(String beanName) {
		synchronized (this.singletonObjects) {
			this.beanNames.remove(beanName);
			this.singletonObjects.remove(beanName);
		}
	}

	public void registerDependentBean(String beanName, String dependentBeanName) {
		Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
		if (dependentBeans != null && dependentBeans.contains(dependentBeanName)) {
			return;
		}

		// No entry yet -> fully synchronized manipulation of the dependentBeans Set
		synchronized (this.dependentBeanMap) {
			dependentBeans = this.dependentBeanMap.get(beanName);
			if (dependentBeans == null) {
				dependentBeans = new LinkedHashSet<String>(8);
				this.dependentBeanMap.put(beanName, dependentBeans);
			}
			dependentBeans.add(dependentBeanName);
		}
		synchronized (this.dependenciesForBeanMap) {
			Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(dependentBeanName);
			if (dependenciesForBean == null) {
				dependenciesForBean = new LinkedHashSet<String>(8);
				this.dependenciesForBeanMap.put(dependentBeanName, dependenciesForBean);
			}
			dependenciesForBean.add(beanName);
		}

	}

	public String[] getDependentBeans(String beanName) {
		Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
		if (dependentBeans == null) {
			return new String[0];
		}
		return (String[]) dependentBeans.toArray();
	}

	public String[] getDependenciesForBean(String beanName) {
		Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(beanName);
		if (dependenciesForBean == null) {
			return new String[0];
		}
		return (String[]) dependenciesForBean.toArray();

	}
}