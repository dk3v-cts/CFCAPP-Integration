package com.cognizant.wschat.event;

import java.util.Date;

/**
 * 
 * @author Dinesh
 */
public class LoginEvent {

	private String username;
	private Date time;
	private String nickname;
	
	public LoginEvent(String username) {
		this.username = username;
		time = new Date();
	}


	public LoginEvent(String username, String nickname) {
		this.username = username;
		time = new Date();
		this.nickname=nickname;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}


	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


}
