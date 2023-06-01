package com.minis.service;

import lombok.Getter;
import lombok.Setter;

/**
 * BaseService
 *
 * @author qizhi
 * @date 2023/06/01
 */
@Getter
@Setter
public class BaseService {

	private BaseBaseService bbs;
	// 省略 getter、setter方法

	public void baseMethod() {
		System.out.println(this.getClass().getName() + ": baseMethod");
	}
}
