package com.poc.ig.repo.test.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String externalId;
	private String tenant;
	private String name;
	private String description;
	private String owner;
	private String organization;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Role(String externalId, String name,String description,String owner,String organization) {
		this.externalId = externalId;
		this.name = name;
		this.description = description;
		this.owner = owner;
		this.organization = organization;
	}
}
