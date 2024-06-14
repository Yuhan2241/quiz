package com.example.quiz.vo;

import java.util.List;

public class FeedbackRes extends BasicRes {

	private List<Feedback> feedbackList;

	public FeedbackRes() {
		super();
	}

	public FeedbackRes(int statusCode, String message) {
		super(statusCode, message);
	}

	public FeedbackRes(int statusCode, String message, List<Feedback> feedbackList) {
		super(statusCode, message);
		this.feedbackList = feedbackList;
	}

	public List<Feedback> getFeedbackList() {
		return feedbackList;
	}
	
	

}
