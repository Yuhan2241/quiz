package com.example.quiz.vo;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchReq {
	
	private int quizId;

	private String name;
	
	@JsonProperty("start_date")
	private LocalDate startDate;
	
	@JsonProperty("end_date")
	private LocalDate endDate;

	public int getQuizId() {
		return quizId;
	}

	public String getName() {
		return name;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}
	
	
}
