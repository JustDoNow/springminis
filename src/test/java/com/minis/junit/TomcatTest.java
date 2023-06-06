package com.minis.junit;

/**
 * TomcatTest
 *
 * @author qizhi
 * @date 2023/06/05
 */
public class TomcatTest {

//	public static void main(String[] args) throws LifecycleException {
//
//		Tomcat tomcat = new Tomcat();
//		tomcat.setPort(8888);
//		tomcat.getConnector();
//
//		tomcat.start();
//		tomcat.getServer().await();
//	}

//	public static void main(String[] args) throws LifecycleException {
//		Tomcat tomcat = new Tomcat();
//		tomcat.setBaseDir("temp");
//		tomcat.setPort(8080);
//		tomcat.getConnector();
//
//		String contextPath = "";
//		String docBase = new File(".").getAbsolutePath();
//
//		Context context = tomcat.addContext(contextPath, docBase);
//
//		class SampleServlet extends HttpServlet {
//
//			@Override
//			protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//					throws ServletException, IOException {
//				PrintWriter writer = resp.getWriter();
//
//				writer.println("<html><title>Welcome</title><body>");
//				writer.println("<h1>Have a Great Day!</h1>");
//				writer.println("</body></html>");
//			}
//		}
//
//		String servletName = "SampleServlet";
//		String urlPattern = "/aa";
//
//		tomcat.addServlet(contextPath, servletName, (Servlet) new SampleServlet());
//		context.addServletMappingDecoded(urlPattern, servletName);
//
//		tomcat.start();
//		tomcat.getServer().await();
//	}
}
