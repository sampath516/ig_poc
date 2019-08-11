package com.poc.ig.repo.test.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class Resource implements Serializable {
	private static final long serialVersionUID = 1L;
	private String externalId;
	private String name;
	private String description;
	private String application;
	
	public Resource(String externalId, String name, String description, String application) {
		this.externalId = externalId;
		this.name = name;
		this.description = description;
		this.application = application;
	}

}
