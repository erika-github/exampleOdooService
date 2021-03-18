package com.example.odooService.utility;

import org.springframework.stereotype.Component;

@Component
public class RespCreateRecord {

	private String status;
	private Integer idRegistro;

	public RespCreateRecord() {

	}

	public RespCreateRecord(String status, Integer idRegistro) {

		this.status = status;
		this.idRegistro = idRegistro;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getIdRegistro() {
		return idRegistro;
	}

	public void setIdRegistro(Integer idRegistro) {
		this.idRegistro = idRegistro;
	}

}
