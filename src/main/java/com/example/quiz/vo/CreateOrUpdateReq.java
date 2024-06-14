package com.example.quiz.vo;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateOrUpdateReq {
	
	private int id;
	
	private String name;

	private String description;
	
	@JsonProperty("start_date")
	private LocalDate startDate;
	
	@JsonProperty("end_date")
	private  LocalDate endDate;
	
	@JsonProperty("question_list")
	//ˆöˆ×—vŒš—§‘½•M‘è–Ú ŠˆÈ—v—pListåä
	private List<Question> questionList;
		
	@JsonProperty("is_published")
	private boolean published;
	
	public CreateOrUpdateReq() {
		super();
	}


	public CreateOrUpdateReq(String name, String description, LocalDate startDate, LocalDate endDate,
			List<Question> questionList, boolean published) {
		super();
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.questionList = questionList;
		this.published = published;
	}


	public CreateOrUpdateReq(int id, String name, String description, LocalDate startDate, LocalDate endDate,
			List<Question> questionList, boolean published) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.questionList = questionList;
		this.published = published;
	}


	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public boolean isPublished() {
		return published;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}


	public int getId() {
		return id;
	}
}
