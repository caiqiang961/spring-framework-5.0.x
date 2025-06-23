package com.caiqiang.simple.conditional;

import com.caiqiang.simple.config.CqConfigx2;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-08-17 14:20
 **/
public class Con1 implements ConfigurationCondition {
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		//获取spring容器
		ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
		//判断容器中是否存在Service类型的bean
		boolean existsService = !beanFactory.getBeansOfType(CqConfigx2.class).isEmpty();
		System.out.println("当前是否存在："+existsService);
		return existsService;
	}

	@Override
	public ConfigurationPhase getConfigurationPhase() {
		return ConfigurationPhase.REGISTER_BEAN;
	}
}
