package com.example.quiz.vo;

public class Statistics { //ˆê‘è“I“Œv

	private int qId;

	private String qTitle;

	private boolean required;

	private String option;

	private int count;

	public Statistics() {
		super();
	}

	public Statistics(int qId, String qTitle, boolean required, String option, int count) {
		super();
		this.qId = qId;
		this.qTitle = qTitle;
		this.required = required;
		this.option = option;
		this.count = count;
	}

	public int getqId() {
		return qId;
	}

	public String getqTitle() {
		return qTitle;
	}

	public boolean isRequired() {
		return required;
	}

	public String getOption() {
		return option;
	}

	public int getCount() {
		return count;
	}

}
