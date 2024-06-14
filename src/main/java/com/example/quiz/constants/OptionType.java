package com.example.quiz.constants;

public enum OptionType {
	SINGLE_CHOICE("radio"),//
	MULTI_CHOICE("checkbox"),//
	TEXT("text");
	
	private String type;

	private OptionType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
}
