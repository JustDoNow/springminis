
package com.minis.web;

import javax.servlet.ServletContext;
import com.minis.context.ClassPathXmlApplicationContext;

public class XmlWebApplicationContext
		extends ClassPathXmlApplicationContext implements WebApplicationContext{
	private ServletContext servletContext;

	public XmlWebApplicationContext(String fileName) {
		super(fileName);
	}

	public ServletContext getServletContext() {
		return this.servletContext;
	}
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}