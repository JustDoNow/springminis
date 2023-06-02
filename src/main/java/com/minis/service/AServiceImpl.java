package com.minis.service;

import com.minis.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.Setter;

/**
 * AServiceImpl
 *
 * @author qizhi
 * @date 2023/06/01
 */
@Getter
@Setter
public class AServiceImpl implements AService {
	private String name;
	private int level;
	private String property1;
	private String property2;

	@Autowired
	private BaseService baseservice;

	public AServiceImpl() {
	}
	public AServiceImpl(String name, int level) {
		this.name = name;
		this.level = level;
		System.out.println(this.name + "," + this.level);
	}
	public void sayHello() {
		System.out.println(this.property1 + "," + this.property2);
	}

	@Override
	public BaseService getRef1() {
		return baseservice;
	}


	// 在此省略property1和property2的setter、getter方法
}
