package com.poc.ig.certification.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
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

	public ListTenantsResponse(Iterable<com.poc.ig.certification.entity.Tenant> tenantsIn) {
		for (com.poc.ig.certification.entity.Tenant tenant : tenantsIn) {
			Tenant tenantDto = new Tenant();
			tenantDto.setName(tenant.getName());
			tenantDto.setDescription(tenant.getDescription());
			tenantDto.setCreatedAt(tenant.getCreatedAt());
			tenantDto.setUpdatedAt(tenant.getUpdatedAt());
			tenants.add(tenantDto);
		}
	}

	@Data
	@Getter
	@Setter
	public static class Tenant implements Serializable {
		private static final long serialVersionUID = 1L;
		private String name;
		private String description;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
		
	}

}
