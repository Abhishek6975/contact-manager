package com.scm.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entity.User;
import com.scm.forms.UserProfileForm;
import com.scm.helpers.Helper;
import com.scm.repositories.UserRepositories;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private  UserRepositories userRepositories;

	// user dashboard page
	@RequestMapping(value = "/dashboard")
	public String UserDashboard() {

		System.out.println("This is DashBoard");
		return "user/dashboard";
	}

	@RequestMapping(value = "/profile")
	public String userProfile(Model model, org.springframework.security.core.Authentication authentication) {

		System.out.println("This is userProfile");
		return "user/profile";
	}
	
	@RequestMapping(value="/profile/view/{userId}")
	public String updateUserFormView(@PathVariable("userId") String userId,Model model) {
		
		System.out.println("this is user profile view");
		
		User user = userService.getUserById(userId).orElse(null);
		
		UserProfileForm userProfileForm = new UserProfileForm();
		
		userProfileForm.setName(user.getName());
		userProfileForm.setPhoneNumber(user.getPhoneNumber());
		userProfileForm.setAbout(user.getAbout());
		userProfileForm.setPicture(user.getProfilePic());
		
		model.addAttribute("userProfileForm", userProfileForm);
		
		model.addAttribute("userId", userId);
		
		
		return "user/user_profileUpdate_view";
	}
	
	@RequestMapping(value="/update/{userId}" ,method=RequestMethod.POST)
	public String updateUserProfile(@PathVariable("userId") String userId, @Valid @ModelAttribute UserProfileForm userProfileForm,
			BindingResult bindingResult ,Model model) {
		
		if (bindingResult.hasErrors()) {
			return "user/user_profileUpdate_view";
		}
		
		User user = userService.getUserById(userId).orElse(null);
		user.setName(userProfileForm.getName());
		user.setPhoneNumber(userProfileForm.getPhoneNumber());
		user.setAbout(userProfileForm.getAbout());
		
		if(userProfileForm.getUserImage() !=null && !userProfileForm.getUserImage().isEmpty()) {
			
			String filename = UUID.randomUUID().toString();
			String imageUrl = imageService.uploadImage(userProfileForm.getUserImage(), filename);
			user.setCloudinaryImagePublicId(filename);
			user.setProfilePic(imageUrl);
			userProfileForm.setPicture(imageUrl);
			
		}
		
		
		userService.updateUser(user);
		model.addAttribute("message", "User Profile Updated!");
		
		 return "user/user_profileUpdate_view";
		
	}
	
	
	@RequestMapping(value = "/updatePassword")
	public String loadUpdatePassword() {
		
		System.out.println("update Password.............");
		
		return "user/update_password";
	}
	
	
	@RequestMapping(value = "/changepassword", method = RequestMethod.POST)
	public String updatePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, HttpSession session ,Principal principal) {
		
		String email = principal.getName();
		
		User user = userService.getUserByEmail(email);
		
		boolean matches = passwordEncoder.matches(oldPassword , user.getPassword());
		
		if(matches) {
			
			user.setPassword(passwordEncoder.encode(newPassword));
			
			User updatePasswordUser = userRepositories.save(user);
			
			if(updatePasswordUser != null) {
				
				session.setAttribute("message", "Password Changed Sucessfully");
			}else {
				
				session.setAttribute("message", "Somthing Wrong on Server");
			}
			
		}else {
			
			session.setAttribute("message", "Old Password Invalid");
		}

		return "redirect:/user/updatePassword";
	}
	
	
}
