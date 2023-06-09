package com.minis.junit;

import com.minis.beans.BeansException;
import com.minis.context.ClassPathXmlApplicationContextV3;
import com.minis.test.service.AService;
import com.minis.test.service.BaseService;

/**
 * ClassPathXmlApplicationContextTest
 *
 * @author qizhi
 * @date 2023/06/01
 */
public class ClassPathXmlApplicationContextV3Test {

//	@Test
	public void testSayHello() {
		ClassPathXmlApplicationContextV3 ctx = new ClassPathXmlApplicationContextV3("beans.xml", true);
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

//	@Test
	public void testAutowired() {

		ClassPathXmlApplicationContextV3 ctx =
				new ClassPathXmlApplicationContextV3("beans.xml", true);

		try {
			BaseService baseService = (BaseService) ctx.getBean("baseservice");
			baseService.getBbs().sayHello();
		} catch (BeansException e) {
			throw new RuntimeException(e);
		}

	}
}
