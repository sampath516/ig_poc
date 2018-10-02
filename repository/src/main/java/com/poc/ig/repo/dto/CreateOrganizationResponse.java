package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.poc.ig.repo.entity.Organization;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateOrganizationResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private String description;
	private long tenantId;

	public CreateOrganizationResponse(Organization org) {
		this.id = org.getId();
		this.name = org.getName();
		this.description = org.getDescription();
		this.tenantId = org.getTenant().getId();
	}
}
