package com.poc.ig.certification.test.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class TenantDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;

	public TenantDto() {
		super();
	}

	public TenantDto(String name, String description) {
		this.name = name;
		this.description = description;
	}
}
