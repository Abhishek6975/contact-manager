package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scm.entity.User;
import com.scm.repositories.UserRepositories;

@Service
public class SecurityCustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepositories userRepositories;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//		UserDetails user = userRepositories.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not Found With this Email"));
		// user have to load
		return userRepositories.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not Found With this Email"));
	}

}
