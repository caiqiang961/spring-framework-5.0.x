package com.caiqiang.spring1.postProcesser;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	public MyBeanFactoryPostProcessor(){
		System.out.println("MyBeanFactoryPostProcessor");
	}
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		//beanFactory.addBeanPostProcessor();//手动添加一个自定义的beanPostProcessor
		//System.out.println("user的beanDefinition：：：：：：：：：："+beanDefinition.getBeanClassName());
	}
}
