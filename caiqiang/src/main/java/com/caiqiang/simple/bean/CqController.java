package com.caiqiang.simple.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-08-12 18:17
 **/
@Component
public class CqController {

	@Autowired
	private CqService cqService;

	@Autowired
	private CqBean1 cqBean1;

	private String name;
	private Integer count;

	//	@Autowired//     构造器不能进行循环引用(单向构造器先进行属性注入的bean就可以循环引用)
//	public CqController(CqService cqService) {
//		System.out.println("CqController的构造器执行");
//	}

	public CqController() {
		System.out.println("无参构造");
	}

	public void test1(){
		System.out.println("执行cqController的方法test1()");
	}

	public void test2(){
		System.out.println(cqService);
		System.out.println(cqBean1);
	}
//	@Autowired
//	public CqController(CqBean1 cqBean1) {
//		System.out.println("cqBean1参构造");
//	}

////	@Autowired
//	public CqController(CqService cqService) {
//		System.out.println("cqService参构造");
//	}
////	@Autowired
//	public CqController(CqService cqService,CqBean1 cqBean1) {
//		System.out.println("cqService、cqBean1参构造");
//	}

//	public CqController(Integer count) {
//		System.out.println("count参构造");
//	}
//
//	public CqController(String name, Integer count) {
//		System.out.println("name、count参构造");
//	}
//	public CqController(String name, Integer count,Integer ccc) {
//		System.out.println("name、count、ccc参构造");
//	}
}
