package com.caiqiang.simple.config;

import com.caiqiang.simple.bean.CqBean1;
import com.caiqiang.simple.bean.CqController;
import com.caiqiang.simple.bean.CqMapper;
import com.caiqiang.simple.conditional.Con1;
import com.caiqiang.simple.conditional.Con2;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-08-12 17:01
 **/
@Configuration
@ComponentScan(value = "com.caiqiang.simple")
@EnableAspectJAutoProxy
//@Order(10000)//配置类排序
public class CqConfig {

//	@Bean
//	public CqMapper cqMapper(){
//		System.out.println("执行cqMapper");
//		return new CqMapper();
//	}
//
//	@Bean
//	public CqBean1 cqBean1(){
//		System.out.println("调用cqMapper()");
//		CqMapper cqMapper = cqMapper();
//		System.out.println(cqMapper);
//		System.out.println("执行CqBean1");
//		return new CqBean1();
//	}

}
