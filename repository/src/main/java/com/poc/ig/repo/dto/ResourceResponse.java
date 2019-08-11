package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.poc.ig.repo.entity.Resource;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ResourceResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String externalId;
	private String tenantName;
	private String name;
	private String description;
	private String application;
	
	public ResourceResponse(Resource resourceEntity) {
		this.id = resourceEntity.getId();
		this.externalId = resourceEntity.getExternalId();
		this.tenantName = resourceEntity.getTenant().getName();
		this.name = resourceEntity.getName();
		this.description = resourceEntity.getDescription();
		this.application = resourceEntity.getApplication().getName();
	}

}
