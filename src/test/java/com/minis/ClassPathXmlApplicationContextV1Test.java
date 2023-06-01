package com.minis;

import org.junit.Test;

import com.minis.service.AService;

/**
 * ClassPathXmlApplicationContextTest
 *
 * @author qizhi
 * @date 2023/06/01
 */
public class ClassPathXmlApplicationContextV1Test {

	@Test
	public void testSayHello() {
		ClassPathXmlApplicationContextV1 ctx = new
				ClassPathXmlApplicationContextV1("beans.xml");
		AService aService = (AService)ctx.getBean("aservice");
		aService.sayHello();
	}
}
