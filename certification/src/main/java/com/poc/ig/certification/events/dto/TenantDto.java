package com.poc.ig.certification.events.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poc.ig.certification.entity.Tenant;

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
	
	@JsonIgnore
	public Tenant tenant() { 
		Tenant tenant = new Tenant();
		tenant.setName(name);
		tenant.setDescription(description);
		return tenant;
	}
}
