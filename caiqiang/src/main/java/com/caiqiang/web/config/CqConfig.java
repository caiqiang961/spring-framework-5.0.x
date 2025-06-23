package com.caiqiang.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-08-12 17:01
 **/
@Configuration
@ComponentScan(value = "com.caiqiang.web")
@EnableTransactionManagement
public class CqConfig {

}
