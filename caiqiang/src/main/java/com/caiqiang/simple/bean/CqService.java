package com.caiqiang.simple.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-08-12 17:03
 **/
@Component
public class CqService {
//	@Autowired
//	public CqService(CqController cqController) {
//	}

		public CqService() {
		System.out.println("CqService的构造器执行");
	}

	@Autowired
	private CqController cqController;

	private String name;

	private int count;
}
