package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entity.User;
import com.scm.repositories.UserRepositories;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserRepositories userRepositories;

	@GetMapping("/verify-email")
	public String VerifyEmail(@RequestParam("token") String token, HttpSession session) {

		User user = userRepositories.findByEmailToken(token).orElse(null);

		if (user != null) {

			if (user.getEmailToken().equals(token)) {
				user.setEmailVerified(true);
				user.setEnabled(true);
				userRepositories.save(user);
				session.setAttribute("message", "You email is verified. Now you can login ");

				return "success_page";

			}

			session.setAttribute("message", "Email not verified ! Token is not associated with user .");

		}

		session.setAttribute("message", "Email not verified ! Token is not associated with user .");

		return "error_page";
	}

}
