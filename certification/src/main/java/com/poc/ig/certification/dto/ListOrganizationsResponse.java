package com.poc.ig.certification.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.poc.ig.certification.entity.Organization;

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
			orgDto.setName(org.getName());
			orgDto.setDescription(org.getDescription());
			orgDto.setTenantName(org.getTenant().getName());
			orgDto.setExternalId(org.getExternalId());
			orgDto.setCreatedAt(org.getCreatedAt());
			orgDto.setUpdatedAt(org.getUpdatedAt());
			organizations.add(orgDto);
		}
	}

	@Data
	@Getter
	@Setter
	public static class OrganizationDto implements Serializable {
		private static final long serialVersionUID = 1L;
		private String externalId;
		private String name;
		private String description;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
		private String tenantName;
	}

}
