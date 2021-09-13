package com.poc.ig.connector.im.events.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter  
@Setter
@NoArgsConstructor
public class CertificationDto implements Serializable {  
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private String tenantName;
	private OrganizationDto organization;
	// User ExternalID
	private UserDto owner;
	private CertificationType certificationType;
	private CertificationState certificationState;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	
	@Data
	@Getter
	@Setter
	@NoArgsConstructor
	public static class OrganizationDto implements Serializable {
		private static final long serialVersionUID = 1L;
		private String externalId;
		private String name;

		OrganizationDto(String externalId, String name) {
			this.externalId = externalId;
			this.name = name;
		}
	}

	@Data
	@Getter
	@Setter
	@NoArgsConstructor
	public static class UserDto implements Serializable {
		private static final long serialVersionUID = 1L;
		private String externalId;
		private String name;

		UserDto(String externalId, String name) {
			this.externalId = externalId;
			this.name = name;
		}
	}
	
	public static enum CertificationState {
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

	public static enum CertificationType {
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
}
