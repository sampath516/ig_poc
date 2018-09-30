package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.poc.ig.repo.entity.Organization;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OrganizationResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String description;
	private Tenant tenant;

	public OrganizationResponse(Organization org) {
		this.id = org.getId();
		this.name = org.getName();
		this.description = org.getDescription();
		this.tenant = new Tenant();
		tenant.setId(org.getTenant().getId());
		tenant.setName(org.getTenant().getName());
		tenant.setDescription(org.getTenant().getDescription());
	}

	@Data
	@Getter
	@Setter
	public static class Tenant implements Serializable {
		private static final long serialVersionUID = 1L;
		private String id;
		private String name;
		private String description;
	}
}
