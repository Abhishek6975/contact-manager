package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.validators.ValidFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileForm {
	
	private String name;
	
	private String phoneNumber;
	
	private String about;
	
	@ValidFile(message = "Invalid File")
	private MultipartFile userImage;
	
	private String picture;

}
