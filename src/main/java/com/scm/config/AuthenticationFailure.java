package com.scm.config;

import java.io.IOException;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.scm.helpers.Message;
import com.scm.helpers.MessageType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthenticationFailure implements AuthenticationFailureHandler{

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		if(exception instanceof DisabledException) {
			// user is disabled
			HttpSession session = request.getSession();
			
			//session.setAttribute("message", Message.builder().content("User is Disabled").type(MessageType.red).build());
			session.setAttribute("message", "User is disabled, Email with varification link is sent on your email id !");
			
			response.sendRedirect("/login");
		}else {
			
			response.sendRedirect("/login?error=true");
		}
		
	}
	

}
