package com.hackNjit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	/*
	User Model
	It includes city, address of user
	 */
	
	private String city;
	private String addressLine1;

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddressLine() {
		return addressLine1;
	}
	public void setAddressLine(String addressLine) {
		this.addressLine1 = addressLine;
	}
	
}
