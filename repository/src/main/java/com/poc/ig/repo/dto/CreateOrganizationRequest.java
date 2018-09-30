package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poc.ig.repo.entity.Organization;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateOrganizationRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String description;

	@JsonIgnore
	public Organization getOrganization() {
		Organization org = new Organization();
		org.setName(name);
		org.setDescription(description);
		return org;
	}

}
