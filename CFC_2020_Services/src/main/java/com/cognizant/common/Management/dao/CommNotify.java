package com.cognizant.common.Management.dao;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COMMNOTIFY")
public class CommNotify {
	@Id
	private Long id;
	private String postedUserName;
	private String receiverUserName;
	private String latitude;
	private String longitude;
	private Date submissionDate;
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
	public Date getSubmissionDate() {
		return submissionDate;
	}
	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}
	
	@Override
	public String toString() {
		return "CommNotify [id=" + id + ", postedUserName=" + postedUserName + ", receiverUserName=" + receiverUserName
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", submissionDate=" + submissionDate + "]";
	}
}
