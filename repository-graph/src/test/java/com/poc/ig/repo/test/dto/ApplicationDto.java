package com.poc.ig.repo.test.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class ApplicationDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String externalId;
	private String name;
	private String description;
	private String organization;
	private String owner;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public ApplicationDto(String externalId, String name, String description, String organization, String owner) {
		this.externalId = externalId;
		this.name = name;
		this.description = description;
		this.organization = organization;
		this.owner = owner;
	}
}
