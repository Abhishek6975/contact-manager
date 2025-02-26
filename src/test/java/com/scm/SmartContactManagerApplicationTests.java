package com.scm;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scm.helpers.Helper;
import com.scm.services.EmailService;

@SpringBootTest
class SmartContactManagerApplicationTests {

	@Test
	void contextLoads() {
	}
	
	
	@Autowired
	private EmailService emailService;
	
	@Test
	void sendEmailTest() {
		
		String emailtokenString = UUID.randomUUID().toString();
		String emailToken = Helper.getLinkeForEmailVerification(emailtokenString);
		
		emailService.sendEmail("abhisheknarkharkhede083@gmail.com",
				"Tetsing Services",
				emailToken);
		
		
	}
	
	

}
