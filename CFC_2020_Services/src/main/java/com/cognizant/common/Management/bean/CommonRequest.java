/**
 * 
 */
package com.cognizant.common.Management.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author 487245
 *
 */
public class CommonRequest {
	
	@JsonProperty("request")
	private Request request;

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}
}