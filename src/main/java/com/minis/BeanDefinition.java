package com.minis;

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
	private String id;
	private String className;

	public BeanDefinition(String id, String className) {
		this.id = id;
		this.className = className;
	}


}