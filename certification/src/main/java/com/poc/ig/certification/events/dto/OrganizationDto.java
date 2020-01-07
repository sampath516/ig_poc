package com.poc.ig.certification.events.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poc.ig.certification.entity.Organization;

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
	
	@JsonIgnore
	public Organization organization() {
		Organization org = new Organization();
		org.setExternalId(externalId);
		org.setName(name);
		org.setDescription(description);
		return org;
	}
}
