package com.example.odooService.utility;

import org.springframework.stereotype.Component;

@Component
public class ResponseLogin {

	private String status;
	private Integer uid;

	public ResponseLogin() {

	}

	public ResponseLogin(String status, Integer uid) {

		this.status = status;
		this.uid = uid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

}
