package com.poc.ig.repo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ListTenantsResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Tenant> tenants = new ArrayList<>();

	public ListTenantsResponse(List<com.poc.ig.repo.entity.Tenant> tenantsIn) {
		for (com.poc.ig.repo.entity.Tenant tenant : tenantsIn) {
			Tenant tenantDto = new Tenant();
			tenantDto.setId(tenant.getId());
			tenantDto.setName(tenant.getName());
			tenantDto.setDescription(tenant.getDescription());
			tenants.add(tenantDto);
		}
	}

	@Data
	@Getter
	@Setter
	public static class Tenant implements Serializable {
		private static final long serialVersionUID = 1L;
		private long id;
		private String name;
		private String description;
	}

}
