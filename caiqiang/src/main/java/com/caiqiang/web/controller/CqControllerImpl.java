package com.caiqiang.web.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: spring-boot-build
 * @description:
 * @author: caiqiang
 * @create: 2022-09-08 11:08
 **/
@Component("/cqControllerImpl")//beanName方式进行访问  beanName必须以/开头  否则不是BeanName的url
public class CqControllerImpl implements Controller {
	@Override
	public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
		System.out.println("==implements Controller方式访问=");
		return null;
	}
}
