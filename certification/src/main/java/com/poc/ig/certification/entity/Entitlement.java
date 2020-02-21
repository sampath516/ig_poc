package com.poc.ig.certification.entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@NodeEntity(label = "entitlement")
public abstract class Entitlement extends AbstractEntity {
	
	private EntitlementState state=EntitlementState.OPEN;
	private ActionType actionType;
	private EntitlementType entitlementType;
	
	@Relationship(type = "CERT_HAS_ENTITLEMENT", direction = Relationship.INCOMING)
	private Certification certification;
	
	@Relationship(type = "REVIEW_BELONGS_TO_ENTMT", direction = Relationship.INCOMING)
	private Set<Review> reviews = new HashSet<Review>();
	
	
	
	
	public static enum ActionType {
		APPROVE(1), REJECT(2);

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
	
	
	
	public static enum EntitlementState {
		OPEN(1), IN_PROGRESS(2), CLOSED(3);
		private int value;

		EntitlementState(int state) {
			this.value = state;
		}

		public int getValue() {
			return value;
		}

		public static EntitlementState fromValue(int value) {
			for (EntitlementState v : values()) {
				if (v.value == value) {
					return v;
				} 
			}
			throw new IllegalArgumentException(
					"Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
		}

	}
	
	public static enum EntitlementType {
		USER_RESOURCE(1), USER_ROLE(2), ROLE_RESOURCE(3);

		private int value;

		private EntitlementType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static EntitlementType fromValue(int value) {
			for (EntitlementType v : values()) {
				if (v.value == value) {
					return v;
				}
			}
			throw new IllegalArgumentException(
					"Unknown Review Action enum type " + value + ", Allowed values are " + Arrays.toString(values()));
		}

	}

}
