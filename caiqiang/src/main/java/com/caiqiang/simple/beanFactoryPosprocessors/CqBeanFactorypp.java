package com.caiqiang.simple.beanFactoryPosprocessors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-08-17 16:12
 **/
@Component
public class CqBeanFactorypp implements BeanFactoryPostProcessor {

	public CqBeanFactorypp() {
		System.out.println("CqBeanFactorypp构造器创建");
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//		GenericBeanDefinition cqController = (GenericBeanDefinition)beanFactory.getBeanDefinition("cqController");
//		cqController.setAutowireMode(3);//构造注入
//		System.out.println("CqBeanFactorypp");
	}

}
