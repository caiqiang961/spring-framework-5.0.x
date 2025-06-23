package com.caiqiang.web.config.handlerAdapter;

import com.caiqiang.web.controller.CqCon;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-09-09 14:18
 **/
@Component
public class CqHandlerAdapter implements HandlerAdapter {
	@Override
	public boolean supports(Object handler) {
		return handler instanceof CqCon;
	}

	@Override
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Object o = ((CqCon) handler).doCqCon(request, response);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("key",o);

		return null;
	}

	@Override
	public long getLastModified(HttpServletRequest request, Object handler) {
		return 0;
	}
}
