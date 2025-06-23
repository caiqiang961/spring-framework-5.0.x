package com.caiqiang.simple;

import com.caiqiang.simple.bean.CqController;
import com.caiqiang.simple.config.CqConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-08-12 17:01
 **/
public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
		ac.register(CqConfig.class);
		ac.refresh();
		CqController bean = ac.getBean(CqController.class);
		bean.test1();
		bean.test2();

	}


}
