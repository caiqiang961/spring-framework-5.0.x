package com.caiqiang.web.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: spring-boot-build
 * @description:
 * @author: caiqiang
 * @create: 2022-09-08 16:04
 **/
@Component("/cqHttpRequestHandlerImpl")
public class CqHttpRequestHandlerImpl implements HttpRequestHandler {
	@Override
	public void handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
		System.out.println("==implements HttpRequestHandler 方式访问=");
	}
}
