package com.poc.ig.repo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.poc.ig.repo.entity.Tenant;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateTenantResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public CreateTenantResponse(Tenant tenant) {
		this.id = tenant.getId();
		this.name = tenant.getName();
		this.description = tenant.getDescription();
		this.createdAt = tenant.getCreatedAt();
		this.updatedAt = tenant.getUpdatedAt();
	}

}
