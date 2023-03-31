package com.example;

import java.util.List;

public class Response {
	List<Area> location;
	String error;

	public List<Area> getLocation() {
		return location;
	}

	public String getError() {
		return error;
	}
}
