package com.caiqiang.spring1.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-03-23 15:35
 **/
@Component
public class User2 {
//	@Autowired
	private UserInterface user;
//	private User user;

	public User2(){
		System.out.println("user2的：construct");
	}

	@PostConstruct//bean的生命周期回调方法，在执行这个bean的postProcessBeforeInitialization方法之后（就要去初始化bean了），去调用这个方法
	public void init(){
		System.out.println("user2的：init;;;;@PostConstruct");
	}
//	public void test(){
//		System.out.println("user2中的user是：" + user);
//	}
}
