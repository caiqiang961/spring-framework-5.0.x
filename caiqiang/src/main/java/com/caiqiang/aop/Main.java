package com.caiqiang.aop;

import com.caiqiang.aop.config.AppConfig;
import com.caiqiang.aop.service.Impl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext();
		ac.register(AppConfig.class);
		ac.refresh();

		Impl service = ac.getBean(Impl.class);
		//User user=ac.getBean(User.class);
		service.myOne();
	}
}
