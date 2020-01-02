package com.poc.ig.certification.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.poc.ig.certification.entity.Organization;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateOrganizationResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private String externalId;
	private String name;
	private String description;
	private String tenantName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;


	public CreateOrganizationResponse(Organization org) {
		this.externalId = org.getExternalId();
		this.name = org.getName();
		this.description = org.getDescription();
		this.tenantName = org.getTenant().getName();
		this.createdAt = org.getCreatedAt();
		this.updatedAt = org.getUpdatedAt();
	}
}
