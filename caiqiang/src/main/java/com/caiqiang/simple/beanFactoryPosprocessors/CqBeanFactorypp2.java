package com.caiqiang.simple.beanFactoryPosprocessors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-08-17 16:12
 **/
@Component
public class CqBeanFactorypp2 implements BeanFactoryPostProcessor, PriorityOrdered {

	public CqBeanFactorypp2() {
		System.out.println("CqBeanFactorypp2构造器创建");
	}
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("CqBeanFactorypp2");
	}

	@Override
	public int getOrder() {
		return 101;
	}
}
