package com.minis;

import org.junit.Test;

import com.minis.context.ClassPathXmlApplicationContext;
import com.minis.exceptions.BeansException;
import com.minis.service.AService;

/**
 * ClassPathXmlApplicationContextTest
 *
 * @author qizhi
 * @date 2023/06/01
 */
public class ClassPathXmlApplicationContextTest {

	@Test
	public void testSayHello() {
		ClassPathXmlApplicationContext ctx = new
				ClassPathXmlApplicationContext("beans.xml");
		AService aService = null;
		try {
			aService = (AService)ctx.getBean("aservice");
			aService.sayHello();
		} catch (BeansException e) {
			throw new RuntimeException(e);
		}
	}
}
