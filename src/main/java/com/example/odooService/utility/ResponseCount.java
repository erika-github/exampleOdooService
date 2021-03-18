package com.example.odooService.utility;

import org.springframework.stereotype.Component;

@Component
public class ResponseCount {

	private String status;
	private Integer cantidadRegistros;

	public ResponseCount() {

	}

	public ResponseCount(String status, Integer cantidadRegistros) {

		this.status = status;
		this.cantidadRegistros = cantidadRegistros;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCantidadRegistros() {
		return cantidadRegistros;
	}

	public void setCantidadRegistros(Integer cantidadRegistros) {
		this.cantidadRegistros = cantidadRegistros;
	}

}
