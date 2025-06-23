package com.caiqiang.web;

import com.caiqiang.web.config.CqConfig;
import com.caiqiang.web.config.handlerMapping.CqHandlerMapping;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;


/**
 * @program: spring
 * @description: 以手动方式去做一个内嵌的tomcat,模拟springboot的底层
 * @author: caiqiang
 * @create: 2022-09-08 11:19
 **/
public class Main {
	public static void main(String[] args) {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(56667);
		try {
			tomcat.addWebapp("/xx", "E:\\tomcatTemp\\");//给一个"/"会被替换成""
		} catch (Exception e) {
			e.printStackTrace();
		}

		AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
		ac.register(CqConfig.class);
//		ac.refresh();//tomcat.start();内部也调用了refresh方法，所以这里可以不调用

		DispatcherServlet dispatcherServlet = new DispatcherServlet(ac);

		Wrapper mvcServlet = tomcat.addServlet("/xx", "cqServlet", dispatcherServlet);//根目录 此处的"/xx"与上面的必须一致

		//是否在启动服务时执行dispatcherServlet的init方法,1表示启动时执行,不加下面这行代码则会在执行第一个请求时执行
		//原理，如果不设置setLoadOnStartup(1),其根本不会初始化spring容器，即tomcat.start();根本不调用refresh方法，自然也无法进行dispatcherServlet的初始化了,也就是说，此时启动的只是一个干净tomcat,根本没有spring,只有在发送第一个请求时，进行spring容器的初始化
		//如果设置setLoadOnStartup(1),其tomcat.start()内部调用了refresh方法
		mvcServlet.setLoadOnStartup(1);//调用dispatcherServlet的init方法 初始化haddler和adpter等
		mvcServlet.addMapping("/*");

		try {
			tomcat.start();
		} catch (LifecycleException e) {
			e.printStackTrace();
		}

		tomcat.getServer().await();//让tomcat去等待连接，不然会闪退
	}
}
