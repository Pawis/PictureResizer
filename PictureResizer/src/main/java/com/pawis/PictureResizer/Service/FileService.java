package com.pawis.PictureResizer.Service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	public List<byte[]> processPicture(MultipartFile file, int weight, int height);
	
	public byte[] reduceSize(MultipartFile file, double width, String axis);
	

}
