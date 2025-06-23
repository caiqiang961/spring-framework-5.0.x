package com.caiqiang.web.controller;

import com.caiqiang.web.listenerEvent.CqApplicationEvent;
import com.caiqiang.web.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: test-springboot
 * @description:
 * @author: caiqiang
 * @create: 2022-08-18 15:54
 **/
@RestController
public class CqController {

	@Autowired
	private ApplicationContext applicationContext;


    @RequestMapping("/test")
    public String test(String name, User user){
        System.out.println("==@RestController方式访问=");
		CqApplicationEvent cqApplicationEvent = new CqApplicationEvent("'new CqApplicationEvent'");
		applicationContext.publishEvent(cqApplicationEvent);
        return "test";
    }
}
