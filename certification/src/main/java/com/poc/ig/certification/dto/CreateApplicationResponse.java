package com.poc.ig.certification.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.poc.ig.certification.entity.Application;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateApplicationResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private String externalId;
	private String name;
	private String description;
	private String tenantName;
	private String owner;
	private String organization;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;


	public CreateApplicationResponse(Application app) {
		this.externalId = app.getExternalId();
		this.name = app.getName();
		this.description = app.getDescription();
		this.tenantName = app.getTenant().getName();
		this.owner = app.getOwner().getName();
		this.organization = app.getOrganization().getName();
		this.createdAt = app.getCreatedAt();
		this.updatedAt = app.getUpdatedAt();
	}
}
