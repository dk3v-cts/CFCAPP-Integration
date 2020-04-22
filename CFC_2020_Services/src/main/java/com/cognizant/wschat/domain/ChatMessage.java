package com.cognizant.wschat.domain;

/**
 * 
 * @author Dinesh
 */
public class ChatMessage {

	private String username;
	private String message;
	private String nickname;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "ChatMessage [user=" + username + ", message=" + message + "]";
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
