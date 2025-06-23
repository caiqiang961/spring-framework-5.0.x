package com.caiqiang.circularDependency.Controller;

import com.caiqiang.circularDependency.config.AutoConfig;
import com.caiqiang.circularDependency.config.Config;
import com.caiqiang.circularDependency.pojo.Axyz;
import com.caiqiang.circularDependency.pojo.Bxyz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: spring-boot-test
 * @description:
 * @author: caiqiang
 * @create: 2022-03-15 14:35
 **/
@Component
public class CaiqiangController {
     @Autowired
    private Bxyz bxyz;

    @Autowired
    private Axyz axyz;

    @RequestMapping("/testCaiqiang")
    public String testCaiqiang(){
        System.out.println("程序运行中的依赖注入bxyz："+bxyz);
        System.out.println("程序运行中的依赖注入axyz："+axyz);
        return "testCaiqiang";
    }
}
