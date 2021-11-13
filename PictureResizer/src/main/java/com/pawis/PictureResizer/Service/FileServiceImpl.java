package com.pawis.PictureResizer.Service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

	@Value("${app.upload.dir}")
	private String uploadDir;

	public List<byte[]> processPicture(MultipartFile file, int weight, int height) {

		List<byte[]> pictures = new ArrayList<>();
		Picture pic = new Picture(convertFileToBufferedImage(file));
		Picture overlaid = new Picture(pic);
		SeamCarver sc = new SeamCarver(pic);
		int[] seam = null;

		for (int i = 0; i < height; i++) {
			seam = sc.findVerticalSeam();
			sc.removeVerticalSeam(seam);
			System.out.println("a");
			for (int col = 0; col < overlaid.height(); col++) {
				overlaid.set(seam[col], col, Color.RED);
			}
		}
		for (int i = 0; i < weight; i++) {
			seam = sc.findHorizontalSeam();
			sc.removeHorizontalSeam(seam);
			System.out.println("b");
			for (int row = 0; row < sc.width(); row++) {
				overlaid.set(row, seam[row], Color.RED);
			}
		}

		byte[] bytes = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(sc.picture().getImage(), "jpg", baos);
			bytes = baos.toByteArray();
			pictures.add(bytes);
			bytes = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] overlaidbytes = null;
		ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
		try {
			ImageIO.write(overlaid.getImage(), "jpg", baos1);
			overlaidbytes = baos1.toByteArray();
			pictures.add(overlaidbytes);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return pictures;
	}

	public byte[] reduceSize(MultipartFile file, double percent, String direction) {

		Picture pic = new Picture(convertFileToBufferedImage(file));
		SeamCarver sc = new SeamCarver(pic);
		int percentage = 0;
		System.out.println(direction);
		if (direction.equals("Horizontal")) {
			percentage = (int) (pic.width() * (percent / 100));
			for (int i = 0; i < percentage; i++) {
				sc.removeVerticalSeam(sc.findVerticalSeam());
			}
		}
		if (direction.equals("Vertical")) {
			percentage = (int) (pic.height() * (percent / 100));
			for (int i = 0; i < percentage; i++) {
				sc.removeHorizontalSeam(sc.findHorizontalSeam());
			}
		} else {
			percentage = (int) (pic.height() * (percent / 100));
			for (int i = 0; i < percentage; i++) {
				sc.removeHorizontalSeam(sc.findHorizontalSeam());
			}
			percentage = (int) (pic.width() * (percent / 100));
			for (int i = 0; i < percentage; i++) {
				sc.removeVerticalSeam(sc.findVerticalSeam());
			}
		
		}
		//File newPic = new File("src/main/resources/Files/" + file.getOriginalFilename());
		//sc.picture().save(newPic);
	

		byte[] bytes = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(sc.picture().getImage(), "jpg", baos);
			bytes = baos.toByteArray();
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}

	private BufferedImage convertFileToBufferedImage(MultipartFile file) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(file.getInputStream());
			file.getInputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}
	/*
	 * public void processPicture(String fileName, int weight, int height) {
	 * 
	 * Picture pic = new
	 * Picture("D:/SpringBoot/PictureResizer/src/main/resources/static/Krzys.jpg");
	 * SeamCarver sc = new SeamCarver(pic); for (int i = 0; i < height; i++)
	 * sc.removeHorizontalSeam(sc.findHorizontalSeam()); for (int i = 0; i < weight;
	 * i++) sc.removeVerticalSeam(sc.findVerticalSeam()); File file = new
	 * File("D:/SpringBoot/PictureResizer/src/main/resources/static/Zbys.jpg");
	 * sc.picture().save(file); }
	 */
}
