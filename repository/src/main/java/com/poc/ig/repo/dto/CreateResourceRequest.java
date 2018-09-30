package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poc.ig.repo.entity.Resource;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateResourceRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String description;
	@JsonIgnore
	public Resource getResource() {
		Resource resource = new Resource();
		resource.setName(this.name);
		resource.setDescription(this.description);
		return resource;
	}

}
