package com.caiqiang.simple.test.visitorModel;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-09-01 16:48
 **/
public class FamilyVistor implements Vistor {
	@Override
	public void getPurpose(DogBirds dogBirds) {
		System.out.println(String.format("家庭单位，购买[%s]付出的价格为[%s]",dogBirds.getName(),dogBirds.getPrice()));
	}

	@Override
	public void payPrice(DogBirds dogBirds) {
		System.out.println(String.format("家庭单位，养[%s]目的是作为宠物，性格要求[%s]",dogBirds.getName(),dogBirds.getDisposition()));
	}
}
