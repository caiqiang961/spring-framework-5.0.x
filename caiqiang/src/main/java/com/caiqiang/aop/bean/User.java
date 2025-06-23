package com.caiqiang.aop.bean;

import org.springframework.stereotype.Component;


@Component
public class User {
	public User(){
		System.out.println("user初始化");
	}
	public String query(){
		return "hello, How are you?";
	}



}
