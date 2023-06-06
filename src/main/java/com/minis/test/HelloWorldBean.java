package com.minis.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * HelloWorldBean
 *
 * @author qizhi
 * @date 2023/06/03
 */
public class HelloWorldBean {
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