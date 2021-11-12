package com.pawis.PictureResizer.Controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.pawis.PictureResizer.Service.FileService;

@Controller
public class FileUploadController {

	@Autowired
	private FileService fileService;

	@GetMapping("/")
	public String homePage() {

		return "index";
	}

	@PostMapping(value = "/processFile", produces = MediaType.IMAGE_JPEG_VALUE)
	public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("percent") String percent,
			@RequestParam("radioName") String axis, Model model) {

		// byte[] newPic = fileService.processPicture(file, w, h);
		// List<byte[]> pictures = fileService.processPicture(file, w, h);
		boolean a = true;
		if (axis.equals("Vertical"))
			a = false;

		double p = Integer.parseInt(percent);
		byte[] pictures = fileService.reduceVerticalSize(file, p, a);
		byte[] orgPic = null;
		try {
			orgPic = file.getBytes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.addAttribute("file", Base64.getEncoder().encodeToString(pictures));
		// model.addAttribute("overlaid",
		// Base64.getEncoder().encodeToString(pictures.get(1)));
		model.addAttribute("ogFile", Base64.getEncoder().encodeToString(orgPic));

		return "display";
	}

}
