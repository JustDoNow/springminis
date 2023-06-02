package com.minis;

import org.junit.Test;

import com.minis.context.ClassPathXmlApplicationContext;
import com.minis.exceptions.BeansException;
import com.minis.service.AService;
import com.minis.service.BaseService;

/**
 * ClassPathXmlApplicationContextTest
 *
 * @author qizhi
 * @date 2023/06/01
 */
public class ClassPathXmlApplicationContextTest {

	@Test
	public void testSayHello() {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml", true);
		AService aService = null;
		try {
			aService = (AService)ctx.getBean("aservice");
			aService.sayHello();

			aService.getRef1().sayHello();

			aService.getRef1().getBbs().baseBaseMethod();

		} catch (BeansException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testAutowired() {

		ClassPathXmlApplicationContext ctx =
				new ClassPathXmlApplicationContext("beans.xml", true);

		try {
			BaseService baseService = (BaseService) ctx.getBean("baseservice");
			baseService.getBbs().sayHello();
		} catch (BeansException e) {
			throw new RuntimeException(e);
		}

	}
}
