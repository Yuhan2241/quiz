package com.example.quiz.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Fillin {

	// question_id
	@JsonProperty("question_id")
	private int qId;

	private String question;

	// ‘½ŒÂ‘I€¥—p;‹øÚ
	private String options;
	// ‘½ŒÂ‰ñ“š¥—p;‹øÚ
	private String answer;

	private String type;

	private boolean required;

	public Fillin() {
		super();
	}



	public Fillin(int qId, String question, String options, String answer, String type, boolean required) {
		super();
		this.qId = qId;
		this.question = question;
		this.options = options;
		this.answer = answer;
		this.type = type;
		this.required = required;
	}

	public int getqId() {
		return qId;
	}

	public String getQuestion() {
		return question;
	}

	public String getOptions() {
		return options;
	}

	public String getAnswer() {
		return answer;
	}

	public String getType() {
		return type;
	}

	public boolean isRequired() {
		return required;
	}

}
