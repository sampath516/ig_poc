package com.poc.ig.repo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.poc.ig.repo.entity.Organization;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ListOrganizationsResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<OrganizationDto> organizations = new ArrayList<>();

	public ListOrganizationsResponse(List<Organization> organizationsIn) {
		for (Organization org : organizationsIn) {
			OrganizationDto orgDto = new OrganizationDto();
			orgDto.setId(org.getId());
			orgDto.setName(org.getName());
			orgDto.setDescription(org.getDescription());
			orgDto.setTenantId(org.getTenant().getId());
			organizations.add(orgDto);
		}
	}

	@Data
	@Getter
	@Setter
	public static class OrganizationDto implements Serializable {
		private static final long serialVersionUID = 1L;
		private String id;
		private String name;
		private String description;
		private String tenantId;
	}

}
