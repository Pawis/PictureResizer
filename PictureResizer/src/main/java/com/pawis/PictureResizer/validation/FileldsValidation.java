package com.pawis.PictureResizer.validation;

import javax.validation.constraints.Pattern;

public class FileldsValidation {

	@Pattern(regexp="[\\d]{3}", message = "Must be number")
	private int height;
	
	@Pattern(regexp="[\\d]{3}")
	private int weight;
	
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
}
