
package com.minis.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 现在完整的过程是：当 Servlet 服务器启动时，Listener 会优先启动，
 * 读配置文件路径，启动过程中初始化上下文，然后启动 IoC 容器，
 * 这个容器通过 refresh() 方法加载所管理的 Bean 对象。
 * 这样就实现了 Tomcat 启动的时候同时启动 IoC 容器。
 */
public class ContextLoaderListener implements ServletContextListener {
	public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";
	private WebApplicationContext context;

	public ContextLoaderListener() {
	}

	public ContextLoaderListener(WebApplicationContext context) {
		this.context = context;
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		initWebApplicationContext(event.getServletContext());
	}

	/**
	 * 通过配置文件参数从 web.xml 中得到配置文件路径，如 applicationContext.xml，
	 * 然后用这个配置文件创建了 AnnotationConfigWebApplicationContext 这一对象，
	 * 我们叫 WAC，这就成了新的上下文。然后调用 servletContext.setAttribute() 方法，
	 * 按照默认的属性值将 WAC 设置到 servletContext 里。
	 * 这样，AnnotationConfigWebApplicationContext 和  servletContext 就能够互相引用了，很方便。
	 *
	 * @param servletContext
	 */
	private void initWebApplicationContext(ServletContext servletContext) {
		String sContextLocation = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
		System.out.println("sContextLocation:" + sContextLocation + ", servletContext:" + servletContext);
		WebApplicationContext wac = new AnnotationConfigWebApplicationContext(sContextLocation);
		wac.setServletContext(servletContext);
		this.context = wac;
		servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
	}
}