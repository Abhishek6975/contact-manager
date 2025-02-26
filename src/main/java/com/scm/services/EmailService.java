package com.scm.services;

public interface EmailService {
	
	void sendEmail(String to , String subjectString , String body) ;
	
	void sendEmailWithHtml();
	
	void sendEmailWithAttachement();

}
