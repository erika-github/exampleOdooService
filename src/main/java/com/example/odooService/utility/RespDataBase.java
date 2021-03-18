package com.example.odooService.utility;

import org.springframework.stereotype.Component;

@Component
public class RespDataBase {

	private String status;
	private String url;
	private String db;
	private String username;
	private String password;

	public RespDataBase() {

	}

	public RespDataBase(String status, String url, String db, String username, String password) {

		this.status = status;
		this.url = url;
		this.db = db;
		this.username = username;
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
