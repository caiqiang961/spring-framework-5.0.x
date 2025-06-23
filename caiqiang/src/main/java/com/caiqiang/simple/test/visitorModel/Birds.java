package com.caiqiang.simple.test.visitorModel;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-09-01 16:46
 **/
public abstract class Birds {

	private String name;
	//品种
	private String breed;
	//性格
	private String disposition;

	public abstract void accept(Vistor vistor);


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public String getDisposition() {
		return disposition;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}
}
