package com.scm.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

	abstract String uploadImage(MultipartFile contactImage);
	abstract String getUrlFromPublicId(String publicId);
	String uploadImage(MultipartFile contactImage, String filename);

}
