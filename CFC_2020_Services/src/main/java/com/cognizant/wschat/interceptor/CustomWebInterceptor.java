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
		if(request.getRequestURL().toString().contains("logout")){
			System.out.println("logout fired");
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if(cookie.getName().equalsIgnoreCase("nickname")){ 
					System.out.println("Cookie found deleted");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
	    	if(null!=request.getHeader("Referer") ) {
	    		String referredUrl = request.getHeader("Referer");
	    		if(referredUrl.contains("nickname=")) {

	    			String[] params = referredUrl.split("nickname=");
	    			if(params.length >1) {
	    				request.getSession().setAttribute("nickname", params[1]);
	    				response.setHeader("nickname",  params[1]);
	    				String cookieVal = params[1];
	    				
	    				Cookie nickNameCookie = new Cookie("nickname",cookieVal);
	    				nickNameCookie.setPath("/");
	    				response.addCookie(nickNameCookie);
	    			}
	    		}
	    	}
	        return true;
	    }
}
