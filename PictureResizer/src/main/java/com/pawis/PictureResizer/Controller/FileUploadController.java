package com.pawis.PictureResizer.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
			@RequestParam("radioName") String direction, Model model) {

		double p = Integer.parseInt(percent);
		byte[] pictures = fileService.reduceSize(file, p, direction);
		byte[] orgPic = null;
		try {
			orgPic = file.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		model.addAttribute("file", Base64.getEncoder().encodeToString(pictures));
		model.addAttribute("ogFile", Base64.getEncoder().encodeToString(orgPic));
		model.addAttribute("fileName",file.getOriginalFilename());

		return "display";
	}
	
	@GetMapping(value = "/downloadNewPic/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody byte[] downloadNewPic(@PathVariable String fileName) throws IOException {
		InputStream in = getClass().getResourceAsStream("/Files/" + fileName);
		return IOUtils.toByteArray(in);
	}

}
