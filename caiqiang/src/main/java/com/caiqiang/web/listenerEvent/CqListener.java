package com.caiqiang.web.listenerEvent;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-09-19 11:33
 **/
@Component
public class CqListener implements ApplicationListener<CqApplicationEvent> {
	@Override
	public void onApplicationEvent(CqApplicationEvent event) {
		Object source = event.getSource();
		System.out.println("执行CqListener中的onApplicationEvent，"+source.toString());
	}
}
