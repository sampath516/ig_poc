package com.poc.ig.repo.events.dto;

import java.io.Serializable;

import com.poc.ig.repo.entity.Tenant;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
public class TenantDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;

	public TenantDto(Tenant tenant) {
		this.name = tenant.getName();
		this.description = tenant.getDescription();
	}

}
