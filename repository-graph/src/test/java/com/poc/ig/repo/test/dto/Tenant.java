package com.poc.ig.repo.test.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Tenant implements Serializable {  

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private String description;

	public Tenant() {
		super();
	}

	public Tenant(String name, String description) {
		this.name = name;
		this.description = description;
	}
}
