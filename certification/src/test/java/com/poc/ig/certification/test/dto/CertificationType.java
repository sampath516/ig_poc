package com.poc.ig.certification.test.dto;

import java.util.Arrays;



public enum CertificationType {
	USER_PREVILEGES(1), ROLE(2), RESOURCE(3), ACCOUNT(4);

	private int value;

	private CertificationType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static CertificationType fromValue(int value) {
		for (CertificationType v : values()) {
			if (v.value == value) {
				return v;
			}
		}
		throw new IllegalArgumentException(
				"Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
	}

}
