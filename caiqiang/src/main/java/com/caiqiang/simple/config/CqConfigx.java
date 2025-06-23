package com.caiqiang.simple.config;

import com.caiqiang.simple.bean.CqController;
import com.caiqiang.simple.bean.CqMapper;
import com.caiqiang.simple.conditional.Con1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-08-12 17:01
 **/
@Configuration
@Conditional({Con1.class})
public class CqConfigx {

	@Bean
	public CqMapper cqMapper(){
		System.out.println("执行cqMapper");
		return new CqMapper();
	}


}
