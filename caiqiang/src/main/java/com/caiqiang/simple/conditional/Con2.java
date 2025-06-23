package com.caiqiang.simple.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-08-17 14:20
 **/
public class Con2 implements Condition {
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		System.out.println("Con2校验");

		return true;
	}
}
