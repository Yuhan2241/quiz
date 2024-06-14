package com.example.quiz.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Question {

	private int id;

	private String title;
	
	private String options;

	private String type;

	@JsonProperty("is_required")
	private boolean required;

	
	
	public Question() {
		super();

	}

	
	public Question(int id, String title, String options, String type, boolean required) {
		super();
		this.id = id;
		this.title = title;
		this.options = options;
		this.type = type;
		this.required = required;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	

	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getOptions() {
		return options;
	}


	public void setOptions(String options) {
		this.options = options;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

}
