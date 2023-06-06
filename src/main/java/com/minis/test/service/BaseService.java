package com.minis.test.service;

import com.minis.beans.factory.annotation.Autowired;

/**
 * BaseService
 *
 * @author qizhi
 * @date 2023/06/01
 */
public class BaseService {
	@Autowired
	private BaseBaseService bbs;

	public BaseBaseService getBbs() {
		return bbs;
	}
	public void setBbs(BaseBaseService bbs) {
		this.bbs = bbs;
	}
	public BaseService() {
	}
	public void sayHello() {
		System.out.println("Base Service says Hello");
		bbs.sayHello();
	}

	public String getHello() {
		return "BaseService.getHello()";
	}
}