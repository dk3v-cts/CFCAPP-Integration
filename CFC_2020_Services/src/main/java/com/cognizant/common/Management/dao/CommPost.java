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
@Table(name = "COMMPOST")
public class CommPost {
	
	@Id
	private int id;
	private String userName;
	private String productType;
	private String subType;
	private String latitude;
	private String longitude;
	private String postType;
	private String status;
	private String quantity;
	private String name;
	
	public CommPost() {
	}
	

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getSubType() {
		return subType;
	}


	public void setSubType(String subType) {
		this.subType = subType;
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


	public String getPostType() {
		return postType;
	}


	public void setPostType(String postType) {
		this.postType = postType;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getQuantity() {
		return quantity;
	}


	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}


	@Override
	public String toString() {
		return String.format("CommPost[id=%d, userName='%s', productType='%s', subType='%s', lattitude='%s', longitude='%s', postType='%s', "
				+ "status='%s', quantity='%s']", id, userName, productType, subType, latitude, longitude, postType, status, quantity);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}