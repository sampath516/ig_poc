package com.poc.ig.repo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.poc.ig.repo.entity.Application;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
public class ApplicationResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String externalId;
	private String name;
	private String description;
	private String owner;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public ApplicationResponse(Application app) {
		this.externalId = app.getExternalId();
		this.name = app.getName();
		this.description = app.getDescription();
		this.owner = app.getOwner().getExternalId();
		this.createdAt = app.getCreatedAt();
		this.updatedAt = app.getUpdatedAt();
	}

}
