package com.caiqiang.spring1.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
//@DependsOn("user2")
public class User implements BeanNameAware, BeanFactoryAware,UserInterface{
//public class User {
	@Autowired
	private User2 user2;

	public User(){
		System.out.println("user的：construct");
	}


	@PostConstruct//bean的生命周期回调方法，在执行这个bean的postProcessBeforeInitialization方法之后（就要去初始化bean了），去调用这个方法
	public void init(){
		System.out.println("user的：init;;;;@PostConstruct");
	}

	@PreDestroy
	public  void distery(){
		System.out.println("user的：distery;;;;;;;;@PreDestroy");
	}

	@Override
	public void setBeanName(String name) {
		System.out.println("执行BeanNameAware接口回调");
		System.out.println();
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("执行BeanFactoryAware接口回调");
		System.out.println();
	}

	@Override
	public String query() {
//		System.out.println("user中的user2是：" + user2);
//		System.out.println("hello, How are you?");
		return "hello, How are you?";
	}
}
