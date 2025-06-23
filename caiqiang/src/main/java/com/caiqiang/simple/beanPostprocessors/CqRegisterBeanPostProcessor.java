package com.caiqiang.simple.beanPostprocessors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-09-01 17:34
 **/
@Component
public class CqRegisterBeanPostProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		System.out.println("CqRegisterBeanPostProcessor--postProcessBeanDefinitionRegistry");
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("CqRegisterBeanPostProcessor--postProcessBeanFactory");
	}

	@Override
	public int getOrder() {
		return 100;
	}
}
