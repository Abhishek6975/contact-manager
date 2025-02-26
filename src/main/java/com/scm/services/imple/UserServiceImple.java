package com.scm.services.imple;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entity.User;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.UserRepositories;
import com.scm.services.EmailService;
import com.scm.services.UserService;

import ch.qos.logback.classic.Logger;
import jakarta.validation.constraints.Null;

@Service
public class UserServiceImple implements UserService {

	@Autowired
	private UserRepositories userRepositories;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService;

	@Override
	public User saveUser(User user) {

		// user id : have to generate
		String userId = UUID.randomUUID().toString(); // if you want to Generated Unique String Id.
		user.setUserId(userId);

		// password Encode/
		// user.setPassword(userId);
		System.out.println(user.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoleList(List.of(AppConstants.ROLE_USER));
		
		String emailToken = UUID.randomUUID().toString();
		user.setEmailToken(emailToken);
		
		User savedUser =  userRepositories.save(user);
		
		String emailLink = Helper.getLinkeForEmailVerification(emailToken);
		
		emailService.sendEmail(savedUser.getEmail(),"Verify Email : Contact Management System", emailLink);
		
		return savedUser;
	}

	@Override
	public Optional<User> getUserById(String id) {

		return userRepositories.findById(id);
	}

	@Override
	public Optional<User> updateUser(User user) {

		User user2 = userRepositories.findById(user.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		user2.setName(user.getName());
		user2.setEmail(user.getEmail());
		user2.setPassword(user.getPassword());
		user2.setAbout(user.getAbout());
		user2.setPhoneNumber(user.getPhoneNumber());
		user2.setProfilePic(user.getProfilePic());
		user2.setEnabled(user.isEnabled());
		user2.setEmailVerified(user.isEmailVerified());
		user2.setPhoneVerified(user.isPhoneVerified());
		user2.setProvider(user.getProvider());
		user2.setProviderUserId(user.getProviderUserId());

		User save = userRepositories.save(user2);
		return Optional.ofNullable(save);

	}

	@Override
	public void deleteUser(String id) {
		User userdelete = userRepositories.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
		userRepositories.delete(userdelete);
	}

	@Override
	public boolean isUserExist(String userId) {
		// TODO Auto-generated method stub
		User user = userRepositories.findById(userId).orElseThrow(null);
		return user != null ? true : false;
	}

	@Override
	public boolean isUserExistByEmail(String email) {
		// TODO Auto-generated method stub
		User user = userRepositories.findByEmail(email).orElseThrow(null);
		return user != null ? true : false;
	}

	@Override
	public List<User> getAllUser() {
		List<User> all = userRepositories.findAll();
		return all;
	}

	@Override
	public User getUserByEmail(String email) {

		return userRepositories.findByEmail(email).orElse(null);
	}

}
