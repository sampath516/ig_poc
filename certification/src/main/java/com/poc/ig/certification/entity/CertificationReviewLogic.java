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
@NodeEntity(label = "cert-review-logic")
public class CertificationReviewLogic extends AbstractEntity {

	private Condition condition = Condition.AND;	
	@Relationship(type = "CERT_ASSOCIATED_REVIEW_LOGIC", direction = Relationship.INCOMING)
	private Certification certification;
	
	
	public static enum Condition {
		AND(1), OR(2), FIRST(3), WEIGHT(4);
		private int value;

		Condition(int state) {
			this.value = state;
		}

		public int getValue() {
			return value;
		}

		public static Condition fromValue(int value) {
			for (Condition v : values()) {
				if (v.value == value) {
					return v;
				}
			}
			throw new IllegalArgumentException(
					"Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
		}

	}
}
