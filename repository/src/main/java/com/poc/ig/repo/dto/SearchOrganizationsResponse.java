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
public class SearchOrganizationsResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<OrganizationResponse> organizations = new ArrayList<>();

	public SearchOrganizationsResponse(List<com.poc.ig.repo.entity.Organization> organizations) {
		for (com.poc.ig.repo.entity.Organization org : organizations) {
//			OrganizationResponse orgDto = new OrganizationResponse(org);
//			orgDto.setOrganizationId(org.getId());
//			orgDto.setOrganizationName(org.getName());
//			Tenant tenantDto = new Tenant();
//			tenantDto.setTenantId(org.getTenant().getId());
//			tenantDto.setTenantName(org.getTenant().getName());
//			orgDto.setTenant(tenantDto);
//			this.organizations.add(orgDto);
		}
	}

}
