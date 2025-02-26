package com.scm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.scm.helpers.MyInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    MyInterceptor myInterceptor() {
		return new MyInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(myInterceptor())
		.addPathPatterns("/user/profile");
	}
    
}
