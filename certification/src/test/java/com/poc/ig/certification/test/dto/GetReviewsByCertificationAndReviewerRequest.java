package com.poc.ig.certification.test.dto;

import java.io.Serializable;
import java.util.Arrays;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Setter
@Getter
@NoArgsConstructor
public class GetReviewsByCertificationAndReviewerRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private ReviewState reviewState;
	
	public static enum ReviewState {
		OPEN(1), CLOSED(2);
		private int value;

		ReviewState(int state) {
			this.value = state;
		}

		public int getValue() {
			return value;
		}

		public static ReviewState fromValue(int value) {
			for (ReviewState v : values()) {
				if (v.value == value) {
					return v;
				}
			}
			throw new IllegalArgumentException(
					"Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
		}

	}

}
