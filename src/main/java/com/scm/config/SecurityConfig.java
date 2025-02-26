package com.scm.config;

import java.io.IOException;

import javax.security.sasl.AuthorizeCallback;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.cache.AlwaysValidCacheEntryValidity;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

//	@Bean
//	public UserDetailsService userDetailsService() {
//		
//		 UserDetails user1 = User.withDefaultPasswordEncoder().username("admin").password("admin123").roles("ADMIN","USER").build();
//		 
//		 UserDetails user2 = User.withDefaultPasswordEncoder().username("admin2").password("admin1234").roles("ADMIN","USER").build();
//		
//		 InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1,user2);
//		 return inMemoryUserDetailsManager;
//		 
//	}

	@Autowired
	private SecurityCustomUserDetailService userDetailService;
	
	@Autowired
	private OAuthenticationSuccessHandler oAuthenticationSuccessHandler;
	
	@Autowired
	private AuthenticationFailure authenticationFailure;

	@Bean
	public AuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		// user detail service ka object:
		daoAuthenticationProvider.setUserDetailsService(userDetailService);
		// password encoder ka object
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

		return daoAuthenticationProvider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		// url's Configure for which are public and which are private
		httpSecurity.authorizeHttpRequests(authorize -> {
//			authorize.requestMatchers("/home","/register","/services").permitAll();
			 authorize.requestMatchers("/user/**").authenticated();
	            authorize.anyRequest().permitAll();
		});

		// form default login
		// form login related
		httpSecurity.formLogin(formLogin -> {
		
			formLogin.loginPage("/login")
			.loginProcessingUrl("/authenticate")
			.successForwardUrl("/user/profile")
//			.failureForwardUrl("/login?error=true")
			.usernameParameter("email")
			.passwordParameter("password")
			.failureUrl("/login?error=true")
			
					/*
					 3 Way's to handle AuthenticationFailure , it is functional Interface 1) used
					 anoynomuous class and override method and provide implementation 2) used
					 lambda function. 3) create new class provide implementation and provide object
				    */
			.failureHandler(authenticationFailure);
			
		});
		
		
		httpSecurity.csrf(AbstractHttpConfigurer::disable);
		
		httpSecurity.logout(logoutForm -> {
			
			logoutForm.logoutUrl("/logout")
			.logoutSuccessUrl("/login?logout=true");
		});
		
		
		httpSecurity.oauth2Login(oauth ->{
			oauth.loginPage("/login")
			.successHandler(oAuthenticationSuccessHandler);
		});
		return httpSecurity.build();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

}
