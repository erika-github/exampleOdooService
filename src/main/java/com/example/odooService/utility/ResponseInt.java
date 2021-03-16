package com.example.odooService.utility;

import org.springframework.stereotype.Component;

@Component
public class ResponseInt {

	private String status;
	private Integer cantidadRegistros;

	public ResponseInt() {

	}

	public ResponseInt(String status, Integer cantidadRegistros) {

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
