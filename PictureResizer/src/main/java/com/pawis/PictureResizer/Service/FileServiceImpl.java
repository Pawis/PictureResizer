package com.pawis.PictureResizer.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

	public byte[] reduceSize(MultipartFile file, double percent, String direction) {

		Picture pic = new Picture(convertFileToBufferedImage(file));
		SeamCarver sc = new SeamCarver(pic);
		int percentage = 0;
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
		}
		if (direction.equals("Both")) {
			percentage = (int) (pic.height() * (percent / 100));
			for (int i = 0; i < percentage; i++) {
				sc.removeHorizontalSeam(sc.findHorizontalSeam());
			}
			percentage = (int) (pic.width() * (percent / 100));
			for (int i = 0; i < percentage; i++) {
				sc.removeVerticalSeam(sc.findVerticalSeam());
			}

		}

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

}
