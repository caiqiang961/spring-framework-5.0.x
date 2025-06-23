package com.caiqiang.simple.test;

import com.caiqiang.simple.config.CqConfig;
import com.caiqiang.simple.config.CqConfigx;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-08-23 16:47
 **/
public class EnTest {
	public static void main(String[] args) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(CqConfigx.class);
		enhancer.setInterfaces(new Class<?>[] {CqTestIn.class});
		enhancer.setUseFactory(false);
		enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);//名字
//		enhancer.setStrategy(new ConfigurationClassEnhancer.BeanFactoryAwareGeneratorStrategy(classLoader));
		//我们可以定义一个过滤器，其中的方法返回的是数组的下标，用来决定当前需要调用数组中哪一个MethodInterceptor方法
		enhancer.setCallbackFilter(new CqCallbackFilter());
		enhancer.setCallbackTypes(new Class<?>[]{CqMethodInterceptor.class,CqMethodInterceptor2.class});
		Enhancer.registerStaticCallbacks(enhancer.createClass(), new Callback[]{new CqMethodInterceptor(),new CqMethodInterceptor2()});


//		enhancer.setCallback(new MethodInterceptor() {
//			@Override
//			public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//				System.err.println("Before invoke " + method);
//				Object result = methodProxy.invokeSuper(o, objects);
//
//				System.err.println("After invoke" + method);
//
//				return result;
//			}
//		});

		CqConfigx o = (CqConfigx)enhancer.create();
		o.cqMapper();
	}
}
