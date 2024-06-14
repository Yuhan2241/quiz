package com.example.quiz.vo;

public class BasicRes {
	
	private int statusCode;
	
	private String message;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BasicRes(int statusCode, String message) {
		super();
		this.statusCode = statusCode;
		this.message = message;
	}

	public BasicRes() {
		super();
	}
	
	
}
