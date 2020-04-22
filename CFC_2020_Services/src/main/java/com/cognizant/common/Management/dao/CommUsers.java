/**
 * 
 */
package com.cognizant.common.Management.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author 487245
 *
 */
@Entity
@Table(name = "COMMUSERS")
public class CommUsers {
	
	@Id
	private Long id;
	private String email;
	private String userName;
	private String password;
	private String loggedin_status;
	
	
	public CommUsers() {
	}
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoggedin_status() {
		return loggedin_status;
	}

	public void setLoggedin_status(String loggedin_status) {
		this.loggedin_status = loggedin_status;
	}

	@Override
	public String toString() {
		return "CommUsers [id=" + id + ", email=" + email + ", userName=" + userName + ", password=" + password
				+ ", loggedin_status=" + loggedin_status + "]";
	}
}