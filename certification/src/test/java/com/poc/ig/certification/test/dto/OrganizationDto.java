package com.poc.ig.certification.test.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OrganizationDto implements Serializable { 

	private static final long serialVersionUID = 1L;

	private String externalId;
	private String name;
	private String description;
	private String tenantName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public OrganizationDto() {
		super();
	}

	public OrganizationDto(String externalId, String name, String description) {
		this.externalId = externalId;
		this.name = name;
		this.description = description;
	}
}
