package com.poc.ig.repo.test.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Resource2 implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String externalId;
	private String name;
	private String description;
	private String application;
	private String tenantName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public Resource2(String externalId, String name, String description, String application) {
		this.externalId = externalId;
		this.name = name;
		this.description = description;
		this.application = application;
	}

}
