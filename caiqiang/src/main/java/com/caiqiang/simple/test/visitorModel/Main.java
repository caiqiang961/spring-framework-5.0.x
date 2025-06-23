package com.caiqiang.simple.test.visitorModel;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-09-01 16:49
 **/
public class Main {

	public static void main(String[] args) {

	FamilyVistor familyVistor = new FamilyVistor();//访问者为家庭
	PoliceVistor policeVistor = new PoliceVistor();//访问者为警察局
	DogBirds dogBirds = new DogBirds();
        dogBirds.setName("中华犬");
        dogBirds.setBreed("强壮");
        dogBirds.setDisposition("开朗");
        dogBirds.setPrice("1000元");

	DogBirds dogBirds2 = new DogBirds();
        dogBirds2.setName("田园犬");
        dogBirds2.setBreed("忠顺");
        dogBirds2.setDisposition("温和");
        dogBirds2.setPrice("2000元");

	DogStructure dogStructure = new DogStructure();//数据结构
        dogStructure.add(dogBirds);
        dogStructure.add(dogBirds2);

        dogStructure.accept(familyVistor);  //家庭访问，并且打印
	//家庭单位，购买[中华犬]付出的价格为[1000元]
	//家庭单位，养[中华犬]目的是作为宠物，性格要求[开朗]
	//家庭单位，购买[田园犬]付出的价格为[2000元]
	//家庭单位，养[田园犬]目的是作为宠物，性格要求[温和]

        System.out.println("-----------------------------------\n");
        dogStructure.accept(policeVistor); //家庭访问，并且打印

	//警察局单位，购买[中华犬]付出的价格为[1000元]
	//警察局单位，养[中华犬]目的是作为警犬，品种要求[强壮]
	//警察局单位，购买[田园犬]付出的价格为[2000元]
	//警察局单位，养[田园犬]目的是作为警犬，品种要求[忠顺]

        System.out.println("-----------------------------------\n");
        dogBirds2.accept(policeVistor);  //单体接受访问者
	}
}
