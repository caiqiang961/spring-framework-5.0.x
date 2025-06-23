package com.caiqiang.simple.test.visitorModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: spring
 * @description:
 * @author: caiqiang
 * @create: 2022-09-01 16:47
 **/
public class DogStructure {
	List<DogBirds> dogStructureList = new ArrayList<DogBirds>();

	public void accept(Vistor vistor){
		for (DogBirds dogBirds : dogStructureList){
			dogBirds.accept(vistor);
		}
	}

	public void add(DogBirds dogBirds) {
		dogStructureList.add(dogBirds);
	}

}
