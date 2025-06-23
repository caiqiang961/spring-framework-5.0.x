package com.caiqiang.simple.test;

import org.springframework.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-08-25 11:35
 **/
public class CqCallbackFilter implements CallbackFilter {
	@Override
	public int accept(Method method) {
		return 1;
	}
}
