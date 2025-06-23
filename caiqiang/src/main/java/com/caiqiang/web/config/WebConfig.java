package com.caiqiang.web.config;

import com.caiqiang.web.config.argumentResolver.CqHandlerMethodArgumentResolver;
import com.caiqiang.web.config.returnValueHandler.CqHandlerMethodReturnValueHandler;
import com.caiqiang.web.interceptor.CqInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleServletHandlerAdapter;

import java.util.List;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-09-08 15:33
 **/
@Configuration
@EnableWebMvc//这个注解在springMvc中有用，在springBoot中没有用，在springMvc中使用他导入的配置类初始化了有BeanNameUrlHandlerMapping和RequestMappingHandlerMapping，并且和springBoot一样的逻辑进行addINceptor的操作，
// （springBoot也是调用了这个类的方法，是一个方法，但是springBoot是自动配置生效，而springMvc需要导入配置生效）
//在springMvc环境中,不加这个注解，我们一旦实现了自己的handlerAdapter或handlerMapping，就会把默认的顶掉，但是加了注解不会，因为已经放在容器了

//增加了这个注解会导致，必须使用tomcat.start()内部的refresh去启动，也就是必须进行启动时disPatcherServlet的初始化
public class WebConfig implements WebMvcConfigurer {
	@Override//二次开发，为什么非要在这里配置呢，我直接把拦截器放在容器中不就ok了吗
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new CqInterceptor());
	}

//	@Bean//用于处理实现了HttpServlet的beanName接口请求
//	public SimpleServletHandlerAdapter simpleServletHandlerAdapter() {
//		return new SimpleServletHandlerAdapter() ;
//	}


	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new CqHandlerMethodArgumentResolver());
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
		handlers.add(new CqHandlerMethodReturnValueHandler());
	}
}
