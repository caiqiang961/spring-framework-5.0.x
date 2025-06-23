package com.caiqiang.aop.service;


import org.springframework.stereotype.Component;

@org.springframework.stereotype.Service
public class Service implements Impl{
    public void myOne(){
//        System.out.println(10/0);
        System.out.println("middle");
    }
}
