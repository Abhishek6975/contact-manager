package com.scm.helpers;

import java.security.Principal;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

	public static String getEmailOfLoggedInUser(org.springframework.security.core.Authentication authentication) {

		// agar emailId password se login kiya hai to : email kaise nikalenge

		if (authentication instanceof OAuth2AuthenticationToken) {

			OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
			String clientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
			
			  OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
			  String username ="";

			if (clientRegistrationId.equalsIgnoreCase("google")) {
				// sign in with google
				System.out.println("Geeting email from google...");
				username= oAuth2User.getAttribute("email").toString();

			} else if (clientRegistrationId.equalsIgnoreCase("github")) {

				System.out.println("Getting email from github...");
			username = oAuth2User.getAttribute("email") != null ? oAuth2User.getAttribute("email").toString()
						: oAuth2User.getAttribute("login").toString() + "@gmail.com";
			}

			// sign in with github
			return username;

		} else {
			System.out.println("Getting Data from local database");
			return authentication.getName();
		}
	}
	
	public static String getLinkeForEmailVerification(String emailToken) {
		
		String link = "http://localhost:8081/auth/verify-email?token=" + emailToken;
		
		return link;
		
	}
}
