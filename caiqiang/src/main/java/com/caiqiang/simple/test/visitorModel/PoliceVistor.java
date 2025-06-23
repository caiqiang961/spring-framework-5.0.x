package com.caiqiang.simple.test.visitorModel;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-09-01 16:48
 **/
public class PoliceVistor implements Vistor {
	@Override
	public void getPurpose(DogBirds dogBirds) {
		System.out.println(String.format("警察局单位，购买[%s]付出的价格为[%s]",dogBirds.getName(),dogBirds.getPrice()));
	}

	@Override
	public void payPrice(DogBirds dogBirds) {
		System.out.println(String.format("警察局单位，养[%s]目的是作为警犬，品种要求[%s]",dogBirds.getName(),dogBirds.getBreed()));
	}
}
