package com.minis.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.test.service.AServiceImpl;
import com.minis.web.RequestMapping;

/**
 * HelloWorldBean
 *
 * @author qizhi
 * @date 2023/06/03
 */
public class HelloWorldBean {

	@Autowired
	private AServiceImpl aService;

	@RequestMapping("/test")
	public String doTest() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = dateFormat.format(new Date());
		String result = aService.getBaseService().getHello() + "test 你好! time:" + format;
		System.out.println(result);
		return result;
	}

	public String doGet() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = dateFormat.format(new Date());
		return "doGet hello world! time:" + format;
	}

	public String doPost() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = dateFormat.format(new Date());
		return "doPost hello world! time:" + format;
	}
}