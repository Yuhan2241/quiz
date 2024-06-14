package com.example.quiz.vo;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FillinReq {

	@JsonProperty("quiz_id")
	private int quizId;

	private String name;

	private String phone;

	private String email;

	private int age;

	@JsonProperty("fillin_list")
	private List<Fillin> fillinList;

	public FillinReq() {
		super();
	}

	public FillinReq(int quizId, String name, String phone, String email, int age, List<Fillin> fillinList) {
		super();
		this.quizId = quizId;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.fillinList = fillinList;
	}

	public int getQuizId() {
		return quizId;
	}

	public String getName() {
		return name;
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
