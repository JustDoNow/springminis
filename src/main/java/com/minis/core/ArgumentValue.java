package com.minis.core;

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
public class ArgumentValue {
	private Object value;
	private String type;
	private String name;
	public ArgumentValue(Object value, String type) {
		this.value = value;
		this.type = type;
	}
	public ArgumentValue(Object value, String type, String name) {
		this.value = value;
		this.type = type;
		this.name = name;
	}
	//省略getter和setter
}