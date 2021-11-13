package com.pawis.PictureResizer.Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.pawis.PictureResizer.Service.FileService;

@Controller
public class FileUploadController {

	@Autowired
	private FileService fileService;
	
	private byte[] pictures;

	@GetMapping("/")
	public String homePage() {

		return "index";
	}


	@PostMapping(value = "/processFile", produces = MediaType.IMAGE_JPEG_VALUE)
	public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("percent") String percent,
			@RequestParam("radioName") String direction, Model model) {

		double p = Integer.parseInt(percent);
		pictures = fileService.reduceSize(file, p, direction);
		byte[] orgPic = null;
		try {
			orgPic = file.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}

		model.addAttribute("file", Base64.getEncoder().encodeToString(pictures));
		model.addAttribute("ogFile", Base64.getEncoder().encodeToString(orgPic));
		model.addAttribute("fileName", file.getOriginalFilename());
		/*
		 * try { Thread.sleep(4 * 1000); } catch (InterruptedException ie) {
		 * Thread.currentThread().interrupt(); }
		 */
		return "display";
	}

	/*
	 * @GetMapping(value = "/downloadNewPic/{fileName}", produces =
	 * MediaType.APPLICATION_OCTET_STREAM_VALUE) public @ResponseBody byte[]
	 * downloadNewPic(@PathVariable String fileName) throws IOException {
	 * InputStream in = getClass().getResourceAsStream("/Files/" + fileName); return
	 * IOUtils.toByteArray(in); }
	 */

	@GetMapping(value = "/downloadNewPic/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> pic(@PathVariable String fileName) throws IOException {

		// InputStream in = getClass().getResourceAsStream("/Files/" + fileName);
		InputStream in1 = new ByteArrayInputStream(pictures);

		HttpHeaders header = new HttpHeaders();

		return new ResponseEntity<byte[]>(IOUtils.toByteArray(in1), header, HttpStatus.CREATED);
	}

}
