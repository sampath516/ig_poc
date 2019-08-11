package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poc.ig.repo.entity.Tenant;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateTenantRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	@JsonIgnore
	public Tenant tenant() { 
		Tenant tenant = new Tenant();
		tenant.setName(name);
		tenant.setDescription(description);
		return tenant;
	}
}
