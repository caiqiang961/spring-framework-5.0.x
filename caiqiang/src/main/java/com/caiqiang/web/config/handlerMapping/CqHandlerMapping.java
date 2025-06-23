package com.caiqiang.web.config.handlerMapping;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-09-09 14:20
 **/
//@Component
public class CqHandlerMapping implements HandlerMapping {
	@Override
	public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
//		HandlerExecutionChain handlerExecutionChain = new HandlerExecutionChain();

		return null;
	}


}
