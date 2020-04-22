package com.cognizant.wschat.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String SECURE_ADMIN_PASSWORD = "rockandroll";
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();
		
		http		
			.csrf().disable()			
			.formLogin()
				.loginPage("/chat/index.html")
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/chat/chat.html")
				.permitAll()
				.and()
			.logout()
				.logoutSuccessUrl("/chat/index.html")
				.permitAll()
				.and()
			.authorizeRequests()
				.antMatchers("/chat/js/**", "/chat/lib/**", "/images/**", "/chat/css/**", "/chat/index.html","/*","/api/auth/**","/api/**","/index.html","/dashboard/**","/auth/**","/not-found/**").permitAll()
				.antMatchers("/websocket").hasRole("ADMIN")
			//	.requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")
				.anyRequest().authenticated();
		System.out.println(http);
				
	}
	
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(new AuthenticationProvider() {
			
			@Override
			public boolean supports(Class<?> authentication) {
				return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
			}
			
			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
				
				List<GrantedAuthority> authorities = SECURE_ADMIN_PASSWORD.equals(token.getCredentials()) ? 
														AuthorityUtils.createAuthorityList("ROLE_ADMIN") : null;
														
				return new UsernamePasswordAuthenticationToken(token.getName(), token.getCredentials(), authorities);
			}
		});
	}
}
