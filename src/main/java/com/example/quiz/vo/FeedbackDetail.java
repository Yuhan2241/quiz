package com.example.quiz.vo;

import java.time.LocalDate;
import java.util.List;

public class FeedbackDetail {
	// quiz
	private LocalDate quizStartDate;

	private LocalDate quizEndDate;

	private String quizName;

	private String quizDescription;

	// response
	private String userName;

	private String phone;

	private String email;

	private int age;

	private List<Fillin> fillinList;

	public FeedbackDetail() {
		super();
	}

	public FeedbackDetail(LocalDate quizStartDate, LocalDate quizEndDate, String quizName, String quizDescription,
			String userName, String phone, String email, int age, List<Fillin> fillinList) {
		super();
		this.quizStartDate = quizStartDate;
		this.quizEndDate = quizEndDate;
		this.quizName = quizName;
		this.quizDescription = quizDescription;
		this.userName = userName;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.fillinList = fillinList;
	}

	public LocalDate getQuizStartDate() {
		return quizStartDate;
	}

	public LocalDate getQuizEndDate() {
		return quizEndDate;
	}

	public String getQuizName() {
		return quizName;
	}

	public String getQuizDescription() {
		return quizDescription;
	}

	public String getUserName() {
		return userName;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public int getAge() {
		return age;
	}

	public List<Fillin> getFillinList() {
		return fillinList;
	}

}
