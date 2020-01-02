package com.poc.ig.certification.test.dto;

import java.io.Serializable;
import java.time.LocalDateTime;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CertificationResponse implements Serializable {
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
}
