package com.pawis.PictureResizer.validation;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.web.multipart.MultipartFile;

import com.pawis.recipes.MyRecipesWebApp.annotation.ValidImage;

public class UploadForm {
	
	@NotNull(message = "Please select picture")
	@ValidImage
	private MultipartFile picture = null;
	
	@NotNull(message= "numericField: positive number value is required")
	@Max(value = 99, message = "Cannot be greater than 99")
	@Min(value =0, message = "Cannot be negative number")
	@Pattern(regexp="^(0|[1-9][0-9]*)$", message="Must be a number")
	private String percent;
	
	@NotNull
	private String direction = "Vertical";

	public UploadForm() {
		
	}
	
	public UploadForm(MultipartFile picture, String percent, String direction) {
		this.picture = picture;
		this.percent = percent;
		this.direction = direction;
	}

	public MultipartFile getPicture() {
		return picture;
	}

	public void setPicture(MultipartFile picture) {
		this.picture = picture;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}
	
}
