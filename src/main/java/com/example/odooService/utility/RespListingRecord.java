package com.example.odooService.utility;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class RespListingRecord {

	private String status;

	private HashMap<String, Object> listingRecords;

	public RespListingRecord() {

	}

	public RespListingRecord(String status, HashMap<String, Object> listingRecords) {

		this.status = status;
		this.listingRecords = listingRecords;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public HashMap<String, Object> getListingRecords() {
		return listingRecords;
	}

	public void setListingRecords(Map<String, Object> listingRecords) {
		this.listingRecords = (HashMap<String, Object>) listingRecords;
	}

}
