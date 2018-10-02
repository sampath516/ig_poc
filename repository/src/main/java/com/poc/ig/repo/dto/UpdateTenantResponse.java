package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.poc.ig.repo.entity.Tenant;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UpdateTenantResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private String description;

	public UpdateTenantResponse(Tenant tenant) {
		this.id = tenant.getId();
		this.name = tenant.getName();
		this.description = tenant.getDescription();
	}

}
