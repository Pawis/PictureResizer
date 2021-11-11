package com.pawis.PictureResizer.Service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	public void processPicture(MultipartFile file, int weight, int height);

}
