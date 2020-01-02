package com.poc.ig.certification.test.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class ResourceResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String externalId;
	private String tenantName;
	private String name;
	private String description;
	private ApplicationDto application;
	private UserDto owner;
	private ResourceDto parent;
	private List<ResourceDto> subResources = new ArrayList<ResourceResponse.ResourceDto>();

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
