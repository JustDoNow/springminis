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
	private final String type;
	private final String name;
	private final Object value;
	private final boolean isRef;

	public PropertyValue(String type, String name, Object value, boolean isRef) {
		this.type = type;
		this.name = name;
		this.value = value;
		this.isRef = isRef;
	}

	public PropertyValue(String pType, String pName, String pValue) {
		this(pType, pName, pValue, false);
	}
}