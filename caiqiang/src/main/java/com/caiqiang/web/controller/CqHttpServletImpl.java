package com.caiqiang.web.controller;

import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

/**
 * @program: spring-boot-build
 * @description:
 * @author: caiqiang
 * @create: 2022-09-08 16:14
 **/
@Component("/cqHttpServletImpl")
public class CqHttpServletImpl extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		System.out.println("==extends HttpServlet方式访问=");
	}
}
