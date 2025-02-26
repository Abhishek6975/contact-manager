package com.scm.config;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entity.Providers;
import com.scm.entity.User;
import com.scm.helpers.AppConstants;
import com.scm.repositories.UserRepositories;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private static final String DefaultOAuth2User = null;

	private static final String OAuth2AuthenticationToken = null;

	@Autowired
	private UserRepositories userRepositories;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		System.out.println("AuthenticationSuccessHandler");

		DefaultOAuth2User oauthuser = (DefaultOAuth2User) authentication.getPrincipal(); // with the help of
																							// getPrincipal() we get all
																							// data from Oauth login

		OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

		String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

		System.out.println(authorizedClientRegistrationId);
		
         // used for checking providers info. through attribute
		oauthuser.getAttributes().forEach((key, value) -> {
			System.out.println(key + "  :  " + value);

		});

		User user = new User();
		user.setUserId(UUID.randomUUID().toString());
		user.setRoleList(List.of(AppConstants.ROLE_USER));
		user.setEmailVerified(true);
		user.setEnabled(true);
		user.setPassword("dummy");

		if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {

			user.setEmail(oauthuser.getAttribute("email").toString());
			user.setProfilePic(oauthuser.getAttribute("picture").toString());
			user.setName(oauthuser.getAttribute("name").toString());
			user.setProviderUserId(oauthuser.getName());
			user.setProvider(Providers.GOOGLE);
			user.setAbout("This account is create using google...");

		} else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

			String email = oauthuser.getAttribute("email") != null ? oauthuser.getAttribute("email").toString()
					: oauthuser.getAttribute("login").toString() + "@gmail.com";

			String picture = oauthuser.getAttribute("avatar_url").toString();
			String name = oauthuser.getAttribute("login").toString();
			String providerUserId = oauthuser.getName();

			user.setEmail(email);
			user.setProfilePic(picture);
			user.setName(name);
			user.setProviderUserId(providerUserId);
			user.setProvider(Providers.GITHUB);
			user.setAbout("This account is create using github...");

		} else if (authorizedClientRegistrationId.equalsIgnoreCase("Linkdin")) {

		} else {

			System.out.println("Unknown Provider");
		}

//		System.out.println(user.getName());
//		
//		user.getAttributes().forEach((key,value) -> {
//			System.out.println("key "+ "value "+ key + value);
//		});

//		System.out.println(user.getAuthorities().toString());

		/*
		 * 
		 * String email = user.getAttribute("email").toString(); String name =
		 * user.getAttribute("name").toString(); String picture =
		 * user.getAttribute("picture").toString();
		 * 
		 * // this Data Save into Data base User user1 = new User();
		 * user1.setEmail(email); user1.setName(name); user1.setProfilePic(picture);
		 * user1.setPassword("password"); user1.setUserId(UUID.randomUUID().toString());
		 * user1.setProvider(Providers.GOOGLE); user1.setEnabled(true);
		 * user1.setEmailVerified(true); user1.setProviderUserId(user.getName());
		 * user1.setRoleList(List.of(AppConstants.ROLE_USER));
		 * user1.setAbout("This Account is Created using Google...");
		 * 
		 * User user2 = userRepositories.findByEmail(email).orElse(null);
		 * 
		 * if(user2 == null) { this.userRepositories.save(user1);
		 * System.out.println("User is saved " + email); }
		 * 
		 */

		User user1 = userRepositories.findByEmail(user.getEmail()).orElse(null);

		if (user1 == null) {
			this.userRepositories.save(user);
			System.out.println("User is saved " + user.getEmail());
		}
		
		// following two ways to redirect after login successful, used any One
		// following.
//		response.sendRedirect("/home");
		new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");

	}

}
