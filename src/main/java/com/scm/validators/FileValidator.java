package com.scm.validators;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<ValidFile,MultipartFile>{
	
	private static final long MAX_FILE_SIZE = 1024 * 1024 * 2;  // 2MB
	
	

	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
		
		 if(file == null || file.isEmpty()) {
			// context.disableDefaultConstraintViolation();
			 //context.buildConstraintViolationWithTemplate("File cannot be empty").addConstraintViolation();
			 return true;
		 }
		 
		 if(file.getSize() > MAX_FILE_SIZE) {
			 context.disableDefaultConstraintViolation();
			 context.buildConstraintViolationWithTemplate("File Size Should be less than 2MB").addConstraintViolation();
			 return false;
		 }
		 
		 // resolution
		 
//		 try {
//			BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
//			
//			if(bufferedImage.getHeight() > 1080 || bufferedImage.getWidth() > 1920) {
//				context.disableDefaultConstraintViolation();
//				context.buildConstraintViolationWithTemplate("Image size should be 1920x1080 pixels or smaller").addConstraintViolation();
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		 
		
		return true;
	}
}
