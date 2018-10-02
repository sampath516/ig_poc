package com.poc.ig.repo.test.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Organization implements Serializable { 

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private String description;
	private String tenantId;

	public Organization() {
		super();
	}

	public Organization(String name, String description) {
		this.name = name;
		this.description = description;
	}
}
