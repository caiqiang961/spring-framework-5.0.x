package com.caiqiang.simple.beanFactoryPosprocessors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-08-19 16:26
 **/
@Component
public class CqBeanFactoryAware implements BeanFactoryAware {
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("CqBeanFactoryAware回调");
	}
}
