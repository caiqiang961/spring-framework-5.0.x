package com.caiqiang.circularDependency;

import com.caiqiang.circularDependency.Controller.CaiqiangController;
import com.caiqiang.circularDependency.config.AutoConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-06-16 14:52
 **/
public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext();
		ac.register(AutoConfig.class);
		ac.refresh();
		ac.getBean(CaiqiangController.class).testCaiqiang();
	}
}
