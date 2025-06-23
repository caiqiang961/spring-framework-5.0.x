package com.caiqiang.circularDependency.config;

import com.caiqiang.circularDependency.pojo.Axyz;
import com.caiqiang.circularDependency.pojo.Bxyz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @program: spring-boot-test
 * @description:
 * @author: caiqiang
 * @create: 2022-06-16 10:56
 **/
@Configuration

public class Config {


    @Autowired
    private Axyz axyz;//在这里我们尽量去使用构造注入更合适,构造注入加载的时间要晚，避免出现null的情况

    public Config() {
        System.out.println("config的构造");
    }

    @Bean
    public Bxyz bxyz(){
        System.out.println("config中创建bxyz");
        System.out.println("config中在依赖的axyz的值为："+axyz);
        return new Bxyz();
    }
}
