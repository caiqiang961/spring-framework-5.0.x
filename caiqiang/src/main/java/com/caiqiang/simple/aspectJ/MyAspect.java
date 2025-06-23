package com.caiqiang.simple.aspectJ;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {
    @Pointcut("execution(* com.caiqiang.simple.bean.CqController.*(..))")
    public void method(){}


    @Before(value = "method()")
    public void before(){
        System.out.println("前置切面。。。。。");
    }

    @After(value = "method()")
    public void after(){
        System.out.println("后置切面。。。。。");
    }
//
//    @Before("Method()" )
//    public void myBefore() {
//            System.out.println("this is my myBefore");
//    }
//    @AfterReturning("Method()")
//    public void myAfterReturn(){
//        System.out.println("this is my myAfterReturn");
//    }
//    @Around("Method()")
//    public Object myAround(ProceedingJoinPoint joinPoint){
//        try {
//            System.out.println("before");
//            joinPoint.proceed();
//            System.out.println("after");
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//            System.out.println("Throws");
//
//        }finally {
//            System.out.println("finally");
//        }
//        return "环绕代理执行return";
//    }

//    @AfterThrowing(value = "Method()" ,throwing = "exception")
//    public void mythrow(Throwable exception){
//        System.out.println("异常");
//    }
}
