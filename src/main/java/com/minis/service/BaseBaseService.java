package com.minis.service;

import lombok.Getter;
import lombok.Setter;

/**
 * BaseBaseService
 *
 * @author qizhi
 * @date 2023/06/01
 */

@Getter
@Setter
public class BaseBaseService {
	private AServiceImpl as;
	// 省略 getter、setter方法


	public void baseBaseMethod() {
		System.out.println(this.getClass().getName() + ": baseBaseMethod");
	}

	public void sayHello() {
		System.out.println(this.getClass().getName() + ".sayHello");
	}
}