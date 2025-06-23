package com.caiqiang.simple.test.visitorModel;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-09-01 16:47
 **/
public class DogBirds extends Birds {


	//价格
	private String price;

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public void accept(Vistor vistor) {
		vistor.getPurpose(this);
		vistor.payPrice(this);
	}

}
