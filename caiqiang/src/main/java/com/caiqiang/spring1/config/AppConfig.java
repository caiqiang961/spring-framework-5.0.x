package com.caiqiang.spring1.config;

import com.caiqiang.spring1.interceptor.MyInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("com.caiqiang.spring1")
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class AppConfig implements WebMvcConfigurer {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		System.out.println("注册拦截器");
		registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**");
	}
}
