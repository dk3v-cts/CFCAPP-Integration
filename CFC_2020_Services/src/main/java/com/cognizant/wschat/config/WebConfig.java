package com.cognizant.wschat.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cognizant.wschat.interceptor.CustomWebInterceptor;


@Component
public class WebConfig implements WebMvcConfigurer {
	
 
	 @Override
	    public void addInterceptors(InterceptorRegistry registry){
	        registry.addInterceptor(new CustomWebInterceptor()).addPathPatterns("/**");
	    }
    

}