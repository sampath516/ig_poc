package com.poc.ig.certification.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.poc.ig.certification.entity.Certification;
import com.poc.ig.certification.entity.Certification.CertificationState;
import com.poc.ig.certification.entity.Certification.CertificationType;

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

	public CertificationResponse(Certification cert) {
		this.name = cert.getName();
		this.description = cert.getDescription();
		this.owner = new UserDto(cert.getOwner().getExternalId(), cert.getOwner().getName());
		this.tenantName = cert.getTenant().getName();
		this.organization = new OrganizationDto(cert.getOrganization().getExternalId(), cert.getOrganization().getName());
		this.certificationType = cert.getType();
		this.certificationState = cert.getState();
		this.createdAt = cert.getCreatedAt();
		this.updatedAt = cert.getUpdatedAt();
	}

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
