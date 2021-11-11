package com.pawis.PictureResizer.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.pawis.PictureResizer.Service.FileService;
import com.pawis.PictureResizer.Service.Picture;

@Controller
public class FileUploadController {

	@Autowired
	private FileService fileService;

	@GetMapping("/")
	public String homePage() {
		return "index";
	}

	@PostMapping("/processFile")
	public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("width") String width,
			@RequestParam("width") String height) {
		int h = Integer.valueOf(height);
		int w = Integer.valueOf(width);
		
		fileService.processPicture(file,w,h);

		return "index";
	}

}
