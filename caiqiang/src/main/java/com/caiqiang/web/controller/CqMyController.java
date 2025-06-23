package com.caiqiang.web.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-09-09 13:57
 **/
@Component("/cqMyController")
public class CqMyController implements CqCon{
	@Override
	@ResponseBody
	public Object doCqCon(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		System.out.println("执行CqMyController 的请求");
		return "哈哈";
	}
}
