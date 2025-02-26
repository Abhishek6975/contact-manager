package com.scm.helpers;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {

	public static void removeMessage(){
		try {
			// Help to remove session
			HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession(); 
			session.removeAttribute("message");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
//	we have RequestContextHolder Class have method getRequestAttributes() which return session Attributes and Then Type Cast 
//	ServletRequestAttributes then get Session  and destroy session Attributes.
}
