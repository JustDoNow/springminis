package com.minis.beans.factory.config;

import lombok.Getter;
import lombok.Setter;

/**
 * ArgumentValue
 *
 * @author qizhi
 * @date 2023/06/01
 */
@Getter
@Setter
public class ConstructorArgumentValue {
	private Object value;
	private String type;
	private String name;

	public ConstructorArgumentValue(String type, String name, Object value) {
		this.value = value;
		this.type = type;
		this.name = name;
	}
	//省略getter和setter
}