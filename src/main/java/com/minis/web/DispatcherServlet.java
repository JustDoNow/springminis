package com.minis.web;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DispatcherServlet
 *
 * @author qizhi
 * @date 2023/06/03
 */
public class DispatcherServlet extends HttpServlet {


	private List<String> packageNames = new ArrayList<>();
	private Map<String, Object> controllerObjs = new HashMap<>();
	private List<String> controllerNames = new ArrayList<>();
	private Map<String, Class<?>> controllerClasses = new HashMap<>();
	private List<String> urlMappingNames = new ArrayList<>();
	private Map<String, Object> mappingObjs = new HashMap<>();
	private Map<String, Method> mappingMethods = new HashMap<>();

	private Map<String, MappingValue> mappingValues;
	private Map<String, Class<?>> mappingClz = new HashMap<>();

	private String sContextConfigLocation;


	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		sContextConfigLocation = config.getInitParameter("contextConfigLocation");

		URL xmlPath = null;
		try {
			xmlPath = this.getServletContext().getResource(sContextConfigLocation);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		this.packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);

		refresh();
	}


	protected void refresh() {
		initController(); // 初始化 controller
		initMapping(); // 初始化 url 映射
	}


	protected void initController() {
		//扫描包，获取所有类名
		this.controllerNames = scanPackages(this.packageNames);
		for (String controllerName : this.controllerNames) {
			Object obj = null;
			Class<?> clz = null;
			try {
				clz = Class.forName(controllerName); //加载类
				this.controllerClasses.put(controllerName, clz);
			} catch (Exception e) {
			}
			try {
				obj = clz.newInstance(); //实例化bean
				this.controllerObjs.put(controllerName, obj);
			} catch (Exception e) {
			}
		}
	}


	private List<String> scanPackages(List<String> packages) {
		List<String> tempControllerNames = new ArrayList<>();
		for (String packageName : packages) {
			tempControllerNames.addAll(scanPackage(packageName));
		}
		return tempControllerNames;
	}
	private List<String> scanPackage(String packageName) {
		List<String> tempControllerNames = new ArrayList<>();
		URI uri = null;
		//将以.分隔的包名换成以/分隔的uri
		try {
			uri = this.getClass().getResource("/" +
					packageName.replaceAll("\\.", "/")).toURI();
		} catch (Exception e) {
		}
		File dir = new File(uri);
		//处理对应的文件目录
		for (File file : dir.listFiles()) { //目录下的文件或者子目录
			if(file.isDirectory()){ //对子目录递归扫描
				scanPackage(packageName+"."+file.getName());
			}else{ //类文件
				String controllerName = packageName +"."
						+file.getName().replace(".class", "");
				tempControllerNames.add(controllerName);
			}
		}
		return tempControllerNames;
	}

	protected void initMapping() {
		for (String controllerName : this.controllerNames) {
			Class<?> clazz = this.controllerClasses.get(controllerName);
			Object obj = this.controllerObjs.get(controllerName);
			Method[] methods = clazz.getDeclaredMethods();
			if (methods != null) {
				for (Method method : methods) {
					//检查所有的方法
					boolean isRequestMapping =
							method.isAnnotationPresent(RequestMapping.class);
					if (isRequestMapping) { //有RequestMapping注解
						String methodName = method.getName();
						//建立方法名和URL的映射
						String urlMapping =
								method.getAnnotation(RequestMapping.class).value();
						this.urlMappingNames.add(urlMapping);
						this.mappingObjs.put(urlMapping, obj);
						this.mappingMethods.put(urlMapping, method);
					}
				}
			}
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sPath = request.getServletPath();
		if (!this.urlMappingNames.contains(sPath)) {
			return;
		}
		Object obj = null;
		Object objResult = null;
		try {
			Method method = this.mappingMethods.get(sPath);
			obj = this.mappingObjs.get(sPath);
			objResult = method.invoke(obj);
		} catch (Exception e) {
		}

		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().append(objResult.toString());
	}

}