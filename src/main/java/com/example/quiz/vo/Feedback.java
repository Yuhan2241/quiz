package com.example.quiz.vo;

import java.time.LocalDateTime;

public class Feedback {
	//回覆列表頁
	private String userName;
	
	private LocalDateTime fillinDateTime;
	
	//查看單獨回覆
	private FeedbackDetail details;

	public Feedback() {
		super();
	}

	public Feedback(String userName, LocalDateTime fillinDateTime, FeedbackDetail details) {
		super();
		this.userName = userName;
		this.fillinDateTime = fillinDateTime;
		this.details = details;
	}

	public String getUserName() {
		return userName;
	}

	public LocalDateTime getFillinDateTime() {
		return fillinDateTime;
	}

	public FeedbackDetail getDetails() {
		return details;
	}
	
}
