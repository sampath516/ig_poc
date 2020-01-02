package com.poc.ig.certification.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.poc.ig.certification.entity.Resource;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
public class ResourceResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private String externalId;
	private String tenantName;
	private String name;
	private String description;
	private ApplicationDto application;
	private UserDto owner;
	private ResourceDto parent;
	private List<ResourceDto> subResources = new ArrayList<ResourceResponse.ResourceDto>();
	
	public ResourceResponse(Resource resourceEntity) {
		this.externalId = resourceEntity.getExternalId();
		this.tenantName = resourceEntity.getTenant().getName();
		this.name = resourceEntity.getName();
		this.description = resourceEntity.getDescription();
		if(resourceEntity.getApplication() != null) {
			application = new ApplicationDto(resourceEntity.getApplication().getExternalId(), resourceEntity.getApplication().getName());
		}
		if(resourceEntity.getOwner() != null) {
			owner = new UserDto(resourceEntity.getOwner().getExternalId(), resourceEntity.getOwner().getName());
		}
		
		if(resourceEntity.getParent() != null) {
			parent = new ResourceDto(resourceEntity.getParent().getExternalId(), resourceEntity.getParent().getName());
		}
		
		if(resourceEntity.getSubResources() != null) {
			for(Resource chield : resourceEntity.getSubResources()) {
				subResources.add(new ResourceDto(chield.getExternalId(), chield.getName()));
			}
		}
	}
	
	@Data
	@Getter
	@Setter
	@NoArgsConstructor
	public static class UserDto {
		private String externalId;
		private String name;

		public UserDto(String externalId, String name) {
			this.externalId = externalId;
			this.name = name;
		}
	}
	
	@Data
	@Getter
	@Setter
	@NoArgsConstructor
	public static class ApplicationDto {
		private String externalId;
		private String name;

		public ApplicationDto(String externalId, String name) {
			this.externalId = externalId;
			this.name = name;
		}
	}
	
	@Data
	@Getter
	@Setter
	@NoArgsConstructor
	public static class ResourceDto {
		private String externalId;
		private String name;
		
		public ResourceDto(String externalId, String name) {
			this.externalId = externalId;
			this.name = name;
		}

	}
}
