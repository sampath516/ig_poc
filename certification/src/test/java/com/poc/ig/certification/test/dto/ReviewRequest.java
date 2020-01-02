package com.poc.ig.certification.test.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	
	private List<String> reviewIds = new ArrayList<String>();
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
	

	public static enum ActionType {
		APPROVE(1), REJECT(2), REASSIGN(3), CONSULT(4);

		private int value;

		private ActionType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static ActionType fromValue(int value) {
			for (ActionType v : values()) {
				if (v.value == value) {
					return v;
				}
			}
			throw new IllegalArgumentException(
					"Unknown Review Action enum type " + value + ", Allowed values are " + Arrays.toString(values()));
		}

	}

}
