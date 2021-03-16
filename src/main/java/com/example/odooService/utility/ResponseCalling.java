package com.example.odooService.utility;

import org.springframework.stereotype.Component;

@Component
public class ResponseCalling {

	private String status;
	private Boolean callingMethods;

	public ResponseCalling() {

	}

	public ResponseCalling(String status, Boolean callingMethods) {

		this.status = status;
		this.callingMethods = callingMethods;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getCallingMethods() {
		return callingMethods;
	}

	public void setCallingMethods(Boolean callingMethods) {
		this.callingMethods = callingMethods;
	}

}
