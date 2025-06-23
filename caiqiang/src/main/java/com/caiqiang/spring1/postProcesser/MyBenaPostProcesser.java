package com.caiqiang.spring1.postProcesser;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class MyBenaPostProcesser implements BeanPostProcessor {

	public MyBenaPostProcesser(){
		System.out.println("MyBenaPostProcesser");
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (beanName.equals("user") || beanName.equals("user2")){
			System.out.println(beanName + "   :   " +"user::::::::::"+bean+":::::::::::::postProcessBeforeInitialization");
		}
		return bean;//
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (beanName.equals("user") || beanName.equals("user2")){
			System.out.println(beanName + "   :   " +"user::::::::::"+bean+":::::::::::::postProcessAfterInitialization");
		}
		return null;
	}
}
