package com.scm.controller;

import java.awt.PageAttributes.ColorType;
import java.lang.ProcessBuilder.Redirect;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entity.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.Message.MessageBuilder;
import com.scm.repositories.UserRepositories;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;
import com.scm.services.imple.UserServiceImple;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

@Controller
public class PageController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepositories userRepositories;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping("/home")
	public String home() {
		System.out.print("This is Home Page");

		return "home";
	}

	@RequestMapping("/about")
	public String about() {
		return "about";
	}

	@RequestMapping("/services")
	public String services() {
		return "services";
	}

	@RequestMapping("/contact")
	public String contact() {
		return "contact";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST) // This method for login with user_name and Password.
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET) // This method for getting Login Page.
	public String loggin() {
		return "login";
	}

	@RequestMapping("/register")
	public String signUp(Model model) {
		UserForm userForm = new UserForm();
		// userForm.setName("abhishek");
		model.addAttribute("userForm", userForm);
		return "register";
	}

	@PostMapping("/do-register")
	// Processing register
	public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult,
			HttpSession session) {
		System.out.println("Processing Registeration");
		// fetch form data
		// 1.user Form

		System.out.println(userForm);

		// 2.Validation Form data
		if (rBindingResult.hasErrors()) {
			return "register";
		}

		// Testing Process
		/*
		 * System.out.println(userForm); System.out.println(userForm.getName());
		 * System.out.println(userForm.getEmail());
		 * System.out.println(userForm.getPassword());
		 * System.out.println(userForm.getPhoneNumber());
		 * System.out.println(userForm.getAbout());
		 */

		// validate form data

		// save to database

		// User user = User.builder()
		// .name(userForm.getName())
		// .email(userForm.getEmail())
		// .password(userForm.getPassword())
		// .about(userForm.getAbout())
		// .phoneNumber(userForm.getPhoneNumber())
		// .profilePic("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.pinterest.com%2Fpin%2F853643304363607717%2F&psig=AOvVaw0KKa6ciwjeNr3PQnEDB1uh&ust=1716054213550000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCOjkvqOelYYDFQAAAAAdAAAAABAE")
		// .build();

		User user = new User();
		user.setName(userForm.getName());
		user.setEmail(userForm.getEmail());
		user.setPassword(userForm.getPassword());
		user.setAbout(userForm.getAbout());
		user.setPhoneNumber(userForm.getPhoneNumber());
		user.setEnabled(false);
		user.setProfilePic(
				"https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.pinterest.com%2Fpin%2F853643304363607717%2F&psig=AOvVaw0KKa6ciwjeNr3PQnEDB1uh&ust=1716054213550000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCOjkvqOelYYDFQAAAAAdAAAAABAE");

		User saveUser = userService.saveUser(user);
		System.out.println("User saved");
		// message = "Registration Successful"
		// return message
		MessageBuilder messageBuilder = Message.builder().content("Registration Successful").type(MessageType.green);

		// MessageBuilder type = Message.builder().type(MessageType.green);

		session.setAttribute("message", "Registration Successful");

		return "redirect:/register"; // if you want to redirect on same view used this technique.
	}

	@RequestMapping(value = "/forgotpassword")
	public String loadForgotPassword() {

		return "forgot_password";
	}

	@RequestMapping(value = "/restpassword")
	public String loadRestPassword() {

		return "reset_password";
	}

	@RequestMapping(value = "/forgotpassword", method = RequestMethod.POST)
	public String forgotPassword(@RequestParam("email") String email, @RequestParam("phoneNumber") String phoneNumber,
			HttpSession session) {

		User user = userRepositories.findByEmailAndPhoneNumber(email, phoneNumber);

		if (user != null) {

			System.out.println("byEmailAndPhoneNumber");

			System.out.println("user resetPassword " + user.getUserId());

			return "redirect:/restpassword/" + user.getUserId();

		} else {

			session.setAttribute("message", "Invalid Email & Mobile No.");

			return "forgot_password";
		}

	}

	@RequestMapping(value = "/restpassword/{userId}")
	public String restPassword(@PathVariable("userId") String userId, HttpSession session) {

		User user = userService.getUserById(userId).orElse(null);

		session.setAttribute("resetPasswordId", user.getUserId());

		return "reset_password";
	}

	@RequestMapping(value = "/restpassword", method = RequestMethod.POST)
	public String changePassword(@RequestParam("newPassword") String newPassword,
			@RequestParam("resetPasswordId") String resetPasswordId, HttpSession session) {

		User user = userService.getUserById(resetPasswordId).orElse(null);

		System.out.println(resetPasswordId);

		user.setPassword(passwordEncoder.encode(newPassword));

		User userPasswordUpdated = userRepositories.save(user);

		if (userPasswordUpdated != null) {

			session.setAttribute("message", "Password Change Successfully");
		}

		return "redirect:/login";
	}

}
