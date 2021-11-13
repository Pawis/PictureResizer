package com.pawis.PictureResizer.Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.pawis.PictureResizer.Service.FileService;
import com.pawis.PictureResizer.validation.UploadForm;

@Controller
public class FileUploadController {

	@Autowired
	private FileService fileService;

	private byte[] pictures;

	@GetMapping("/")
	public String homePage(Model model) {
		model.addAttribute("percentField", new UploadForm());
		return "index";
	}

	@PostMapping(value = "/processFile",  consumes = {"multipart/form-data"}  ,produces = MediaType.IMAGE_JPEG_VALUE)
	public String uploadFile(@Valid @ModelAttribute("percentField") UploadForm percentField, BindingResult br, Model model) {
		
		if (br.hasErrors()) {
			return "index";
		}
		
		double p = Integer.parseInt(percentField.getPercent());
		pictures = fileService.reduceSize(percentField.getPicture(), p, percentField.getDirection());
		byte[] orgPic = null;
		try {
			orgPic = percentField.getPicture().getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}

		model.addAttribute("file", Base64.getEncoder().encodeToString(pictures));
		model.addAttribute("ogFile", Base64.getEncoder().encodeToString(orgPic));
		model.addAttribute("fileName", percentField.getPicture().getOriginalFilename());
		return "display";
	}

	@GetMapping(value = "/downloadNewPic/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> pic(@PathVariable String fileName) throws IOException {

		InputStream in1 = new ByteArrayInputStream(pictures);

		HttpHeaders header = new HttpHeaders();

		return new ResponseEntity<byte[]>(IOUtils.toByteArray(in1), header, HttpStatus.CREATED);
	}

}
