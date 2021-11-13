package com.pawis.PictureResizer.Service;



import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	public byte[] reduceSize(MultipartFile file, double width, String axis);
	

}
