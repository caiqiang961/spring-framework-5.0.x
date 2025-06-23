package com.caiqiang.web.listenerEvent;

import org.springframework.context.ApplicationEvent;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-09-19 11:34
 **/
public class CqApplicationEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	public CqApplicationEvent(Object source) {
		super(source);
	}
}
