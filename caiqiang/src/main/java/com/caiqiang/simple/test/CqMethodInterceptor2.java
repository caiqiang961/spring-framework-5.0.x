package com.caiqiang.simple.test;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-08-25 11:43
 **/
public class CqMethodInterceptor2 implements MethodInterceptor {
	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		System.out.println("xxxBefore invoke " + method);
		Object result = methodProxy.invokeSuper(o, objects);
		System.out.println("xxxAfter invoke" + method);
		return result;
	}
}
