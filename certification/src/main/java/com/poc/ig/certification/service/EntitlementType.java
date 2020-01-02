package com.poc.ig.certification.service;

import java.util.Arrays;

public enum EntitlementType {
	USER_PRIVILEGE_ROLE(1), USER_PRIVILEGE_RES(2), RESOURCE(3), ROLE(4), ACCOUNT(5);

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
				"Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
	}

}
