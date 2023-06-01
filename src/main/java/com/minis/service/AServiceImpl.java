package com.minis.service;

/**
 * AServiceImpl
 *
 * @author qizhi
 * @date 2023/06/01
 */
public class AServiceImpl implements AService {

	private String property1;

	public void setProperty1(String property1) {
		this.property1 = property1;
	}

	public void sayHello() {
		System.out.println("a service 1 say hello");
	}
}
