package com.poc.ig.certification.dto;

import java.io.Serializable;
import java.util.List;

import com.poc.ig.certification.entity.Review.ActionType;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
public class ReviewRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<String> reviewIds;
	private Action action;
	
	
	@Data
	@Setter
	@Getter
	@NoArgsConstructor
	public static class Action implements Serializable {
		private static final long serialVersionUID = 1L;
		private ActionType type;
		private String newReviewer;
		private String comments;
	}
	



}
