package com.poc.ig.repo.events.dto;

import java.io.Serializable;

import com.poc.ig.repo.entity.Organization;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class OrganizationDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String externalId;
	private String name;
	private String description;
	private String tenantName;

	public OrganizationDto(Organization org) {
		this.externalId = org.getExternalId();
		this.name = org.getName();
		this.description = org.getDescription();
		this.tenantName = org.getTenant().getName();

	}
}
