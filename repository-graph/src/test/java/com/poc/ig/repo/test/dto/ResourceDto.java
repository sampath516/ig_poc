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
public class ResourceDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String externalId;
	private String name;
	private String description;
	private String owner;
	private String application;
	
	public ResourceDto(String externalId, String name, String description, String owner, String application) {
		this.externalId = externalId;
		this.name = name;
		this.description = description;
		this.owner = owner;
		this.application = application;
	}

}
