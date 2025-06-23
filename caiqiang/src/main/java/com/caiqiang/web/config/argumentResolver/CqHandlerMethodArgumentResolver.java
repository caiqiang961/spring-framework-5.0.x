package com.caiqiang.web.config.argumentResolver;

import com.caiqiang.web.pojo.User;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-09-15 17:06
 **/
public class CqHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override//该方法表示是否启用这个参数解析器，返回 true 表示启用，返回 false 表示不启用。
	public boolean supportsParameter(MethodParameter parameter) {//请求的对应的方法上的参数会循环调用这个方法，以判断是否调用下面的方法
		System.out.println("CqHandlerMethodArgumentResolver.supportsParameter");
		return true;
	}

	@Override//循环调用这个方法，每次进入即为当前请求的第i个参数
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		System.out.println("CqHandlerMethodArgumentResolver.resolveArgument");
		if (parameter.getParameterType().equals(String.class)){
			return "caiqiang";
		}
		if (parameter.getParameterType().equals(User.class)){
			User user = new User();
			user.setDesc("描述");
			user.setId(100);
			return user;//返回为具体的参数值
		}
		return null;
	}
}
