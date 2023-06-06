package com.minis.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.minis.web.RequestMapping;

/**
 * HelloWorldBean
 *
 * @author qizhi
 * @date 2023/06/03
 */
public class HelloWorldBean {

	@RequestMapping("/test")
	public String doTest() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = dateFormat.format(new Date());
		return "test 你好! time:" + format;
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