package com.example.odooService.utility;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Response_list {

	private String status;

	private List<Object> listado = Arrays.asList();

	public Response_list() {

	}

	public Response_list(String status, List<Object> listado) {

		this.status = status;
		this.listado = listado;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Object> getListado() {
		return listado;
	}

	public void setListado(List<Object> listado) {
		this.listado = listado;
	}

}
