package com.scm.helpers;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String email = request.getParameter("email");
		System.out.println("email " + email);
		
		if(email != null  &&  email.startsWith("vanita")) {
			System.out.println("This email is Blocked!");
			return false;
		}
		System.out.println("Login Succc");
		
		return true;
	}

}
