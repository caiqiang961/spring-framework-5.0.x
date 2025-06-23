package com.caiqiang.circularDependency.config;

import com.caiqiang.circularDependency.pojo.Axyz;
import com.caiqiang.circularDependency.pojo.Bxyz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @program: spring-boot-test
 * @description:
 * @author: caiqiang
 * @create: 2022-03-15 17:23
 **/
@Configuration
//@Import(Config.class)
@ComponentScan("com.caiqiang.circularDependency")
public class AutoConfig {
    @Autowired
    private Bxyz bxyz;

    public AutoConfig() {
        System.out.println("AutoConfig的构造");
    }

    @Bean
    public Axyz axyz(){
        System.out.println("AutoConfig中创建axyz");
        System.out.println("AutoConfig中在依赖的bxyz的值为："+bxyz);
        return new Axyz();
    }

}
