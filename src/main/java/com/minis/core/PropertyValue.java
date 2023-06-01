package com.minis.core;

import lombok.Getter;
import lombok.Setter;

/**
 * PropertyValue
 *
 * @author qizhi
 * @date 2023/06/01
 */
@Getter
@Setter
public class PropertyValue {
	private final String name;
	private final Object value;
	public PropertyValue(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	//省略getter
}