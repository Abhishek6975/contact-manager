package com.scm.services.imple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.scm.services.EmailService;

@Service
public class EmailServiceImpl implements EmailService{
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.properties.domain_name}")
	private String domainName;

	@Override
	public void sendEmail(String to, String subjectString, String body) {
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(to);
		simpleMailMessage.setSubject(subjectString);
		simpleMailMessage.setText(body);
		simpleMailMessage.setFrom(domainName);
		
		javaMailSender.send(simpleMailMessage);
			
		
	}

	@Override
	public void sendEmailWithHtml() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendEmailWithAttachement() {
		// TODO Auto-generated method stub
		
	}
	

}
