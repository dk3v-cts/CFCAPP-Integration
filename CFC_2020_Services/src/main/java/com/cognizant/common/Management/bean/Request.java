/**
 * 
 */
package com.cognizant.common.Management.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author 487245
 *
 */
public class Request {
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("quantity")
	private String quantity;
	
	@JsonProperty("subType")
	private String subType;
	
	@JsonProperty("userName")
	private String userName;
	
	@JsonProperty("latitude")
	private String latitude;
	
	@JsonProperty("longitude")
	private String longitude;
	
	@JsonProperty("requestType")
	private String requestType;
	
	@JsonProperty("password")
	private String password;
	
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("postedUserName")
	private String postedUserName;
	
	@JsonProperty("receiverUserName")
	private String receiverUserName;
	
	@JsonProperty("currentDate")
	private String currentDate;
	
	@JsonProperty("submittedDate")
	private Date submittedDate;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPostedUserName() {
		return postedUserName;
	}

	public void setPostedUserName(String postedUserName) {
		this.postedUserName = postedUserName;
	}

	public String getReceiverUserName() {
		return receiverUserName;
	}

	public void setReceiverUserName(String receiverUserName) {
		this.receiverUserName = receiverUserName;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public Date getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}
}