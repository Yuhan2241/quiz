package com.example.quiz.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteReq {

	@JsonProperty("id_list")
	private List<Integer> idList;

	public DeleteReq() {
		super();
	}

	public DeleteReq(List<Integer> idList) {
		this.idList = idList;
	}

	public List<Integer> getIdList() {
		return idList;
	}
	
	
}
