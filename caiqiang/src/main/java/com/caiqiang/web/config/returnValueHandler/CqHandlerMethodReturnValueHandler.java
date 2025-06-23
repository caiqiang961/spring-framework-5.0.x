package com.caiqiang.web.config.returnValueHandler;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-09-16 10:23
 **/
@Component
public class CqHandlerMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		System.out.println("CqHandlerMethodReturnValueHandler.supportsReturnType");
		return true;
	}

	@Override//将返回值写页面
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
		System.out.println("CqHandlerMethodReturnValueHandler.handleReturnValue");

	}
}
