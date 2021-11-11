package com.pawis.PictureResizer.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

	@Value("${app.upload.dir:${user.home}}")
	private String uploadDir;

	private void save(MultipartFile file) {
		try {
			BufferedImage img = ImageIO.read(file.getInputStream());
			Path copyLocation = Paths.get(uploadDir + File.separator + file.getOriginalFilename() );
			System.out.println(copyLocation.toString());
			Files.copy(file.getInputStream(), copyLocation);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	private BufferedImage convertFile(MultipartFile file) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(file.getInputStream());
			file.getInputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return img;
	}
	*/

	@Override
	public void processPicture(MultipartFile file, int weight, int height) {
		save(file);
		/*
		Picture pic = new Picture(convertFile(file),file.getName());
		SeamCarver sc = new SeamCarver(pic);
		for (int i = 0; i < height; i++)
			sc.removeHorizontalSeam(sc.findHorizontalSeam());
		for (int i = 0; i < weight; i++)
			sc.removeVerticalSeam(sc.findVerticalSeam());
		*/
	}

}
