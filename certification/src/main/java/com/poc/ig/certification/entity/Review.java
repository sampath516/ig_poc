package com.poc.ig.certification.entity;

import java.util.Arrays;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@NodeEntity(label = "review")
public class Review  extends AbstractEntity {

	private ReviewState state = ReviewState.OPEN;
	private ActionType actionType;
	private ReviewLogic logic = ReviewLogic.AND;
	
	
	@Relationship(type = "REVIEW_BELONGS_TO_ENTMT", direction = Relationship.OUTGOING)
	private Entitlement entitlement;
	
	@Relationship(type = "OWNER_OF_REVIEW", direction = Relationship.INCOMING)
	private User reviewer;
	
	
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
	
	public static enum ReviewLogic {
		AND(1), OR(2), WEIGHT(3);
		private int value;

		ReviewLogic(int state) {
			this.value = state;
		}

		public int getValue() {
			return value;
		}

		public static ReviewLogic fromValue(int value) {
			for (ReviewLogic v : values()) {
				if (v.value == value) {
					return v;
				}
			}
			throw new IllegalArgumentException(
					"Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
		}

	}
}
