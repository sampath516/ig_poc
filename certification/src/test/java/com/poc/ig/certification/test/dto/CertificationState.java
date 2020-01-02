package com.poc.ig.certification.test.dto;

import java.util.Arrays;



public enum CertificationState {
	CREATE(1), IN_PROGRESS(2), STOPPED(3), COMPLETED(4);
	private int value;

	CertificationState(int state) {
		this.value = state;
	}

	public int getValue() {
		return value;
	}

	public static CertificationState fromValue(int value) {
		for (CertificationState v : values()) {
			if (v.value == value) {
				return v;
			}
		}
		throw new IllegalArgumentException(
				"Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
	}

}
