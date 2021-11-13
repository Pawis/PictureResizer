package com.pawis.PictureResizer.expection;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.pawis.PictureResizer.validation.UploadForm;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadException(MaxUploadSizeExceededException maxUploadSizeExceededException, Model model){
    	
    
    	model.addAttribute("percentField", new UploadForm());
    	model.addAttribute("error","File is too large - Max 5MB");
		
    	System.out.println("Inside handle max file upload size..");
        return "index";
    }
}
