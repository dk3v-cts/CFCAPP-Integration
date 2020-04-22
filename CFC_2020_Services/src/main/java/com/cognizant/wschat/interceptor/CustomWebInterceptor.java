package com.cognizant.wschat.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class CustomWebInterceptor implements HandlerInterceptor {
	
	@Override
	 public boolean preHandle (HttpServletRequest request, HttpServletResponse response, Object handler) {
		System.out.println("preHandle");
	    	if(null!=request.getHeader("Referer")) {
	    		String referredUrl = request.getHeader("Referer");
	    		if(referredUrl.contains("nickname=")) {

	    			String[] params = referredUrl.split("nickname=");
	    			if(params.length >1) {
	    				request.getSession().setAttribute("nickname", params[1]);
	    				response.setHeader("nickname",  params[1]);
	    				Cookie nickNameCookie = new Cookie("nickname",params[1]);
	    				nickNameCookie.setPath("/");
	    				response.addCookie(new Cookie("nickname",params[1]));
	    			}
	    		}
	    	}
	        return true;
	    }
}
