package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entity.User;
import com.scm.helpers.Helper;
import com.scm.services.UserService;

@ControllerAdvice
public class RootController {

	@Autowired
	private UserService userService;

	@ModelAttribute // with the help of ModelAttribute we Execute Method before all request, so this
					// method check request is authenticate or not, if it is then execute this method(Only authenticate Request).
	public void addLoggedInUserInformation(Model model, Authentication authentication) {

		if (authentication == null) {
			return;
		}

		System.out.println("Adding logged in user information to the model...");
		String username = Helper.getEmailOfLoggedInUser(authentication);
		System.out.println("This is UserName : " + username);

		User user = userService.getUserByEmail(username);

		System.out.println("This is User : " + user);

		System.out.println(user.getName());
		System.out.println(user.getEmail());
		model.addAttribute("loggedInUser", user);
	}
}
