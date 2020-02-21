package com.poc.ig.repo.events.dto;

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
public class EntitlementsRejectedEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String tenant;
	private EntitlementType entitlementType;
	private List<Entitlement> entitlements = new ArrayList<>();

	@Data
	@Setter
	@Getter
	@NoArgsConstructor
	public static class Entitlement implements Serializable{
		private static final long serialVersionUID = 1L;
		
		private String primaryEntityExternalId;
		private String secondaryEntityExternalId;

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
