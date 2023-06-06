package com.minis.web;

import lombok.Getter;
import lombok.Setter;

/**
 * MappingValue
 *
 * @author qizhi
 * @date 2023/06/03
 */
@Getter
@Setter
public class MappingValue {
	String uri;
	String clz;
	String method;

	public MappingValue(String uri, String clz, String method) {
		this.uri = uri;
		this.clz = clz;
		this.method = method;
	}
}
