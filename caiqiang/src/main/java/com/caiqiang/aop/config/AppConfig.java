package com.caiqiang.aop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@ComponentScan("com.caiqiang.aop")
@EnableAspectJAutoProxy
public class AppConfig {

	@Bean
	public CharacterEncodingFilter setEncoding(){
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter("utf-8");

		return characterEncodingFilter;
	}

}
