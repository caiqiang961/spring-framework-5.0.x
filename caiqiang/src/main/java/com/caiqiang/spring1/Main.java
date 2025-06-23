package com.caiqiang.spring1;

import com.caiqiang.spring1.bean.User;
import com.caiqiang.spring1.bean.User2;
import com.caiqiang.spring1.bean.UserInterface;
import com.caiqiang.spring1.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;



public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext();
		//  // 将包扫描配置类注册进BeanDefinitionMap中
		ac.register(AppConfig.class);
		ac.refresh();

//		UserInterface user = ac.getBean(UserInterface.class);
//		User2 user2 = ac.getBean(User2.class);
//		user2.test();
//		user.query();
	}
}
