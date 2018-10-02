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
public class UpdateTenantRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private String description;
	@JsonIgnore
	public Tenant getTenant() {
		Tenant tenant = new Tenant();
		tenant.setId(id);
		tenant.setName(name);
		tenant.setDescription(description);
		return tenant;
	}
}
