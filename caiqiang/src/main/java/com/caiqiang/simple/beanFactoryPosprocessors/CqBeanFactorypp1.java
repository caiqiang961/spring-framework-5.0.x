package com.caiqiang.simple.beanFactoryPosprocessors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * @program: spring
 * @description: PriorityOrdered、ordered
 * @author: caiqiang
 * @create: 2022-08-17 16:12
 **/
@Component
public class CqBeanFactorypp1 implements BeanFactoryPostProcessor, PriorityOrdered {

	public CqBeanFactorypp1() {
		System.out.println("CqBeanFactorypp1构造器创建");
	}
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		System.out.println("CqBeanFactorypp1");
	}

	@Override
	public int getOrder() {
		return 100;//越小越优先
	}
}
