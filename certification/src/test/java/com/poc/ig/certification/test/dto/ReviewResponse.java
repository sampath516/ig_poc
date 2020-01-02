package com.poc.ig.certification.test.dto;

import java.io.Serializable;
import java.util.Arrays;

import com.poc.ig.certification.entity.Review.ReviewState;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
public class ReviewResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private String reviewId;
	private ReviewState reviewState;
	private Entity primaryEntity;
	private Entity secondaryEntity;

	@Data
	@Getter
	@Setter
	public static class Entity implements Serializable {
		private static final long serialVersionUID = 1L;
		private EntityType entityType;;
		private String externalId;
		private String name;
		private String description;
		private String firstName;
		private String lastName;
		private String organization;
		private String application;

	}

	public static enum EntityType {
		USER(1), ROLE(2), RESOURCE(3), APPLICATION(4);

		private int value;

		private EntityType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static EntityType fromValue(int value) {
			for (EntityType v : values()) {
				if (v.value == value) {
					return v;
				}
			}
			throw new IllegalArgumentException(
					"Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
		}

	}

}
