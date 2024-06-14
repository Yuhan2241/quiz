package com.example.quiz.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FeedbackReq {

	@JsonProperty("quiz_id")
	private int quizId;

	public int getQuizId() {
		return quizId;
	}

	public FeedbackReq() {
		super();

	}

	public FeedbackReq(int quizId) {
		super();
		this.quizId = quizId;
	}

}
