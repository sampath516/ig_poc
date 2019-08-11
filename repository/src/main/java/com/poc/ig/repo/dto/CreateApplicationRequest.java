package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poc.ig.repo.entity.Application;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateApplicationRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private String externalId;
	private String name;
	private String description;
	private String organization;
	private String owner;


	@JsonIgnore
	public Application getApplication() {
		Application app = new Application();
		app.setExternalId(externalId);
		app.setName(name);
		app.setDescription(description);
		return app;
	}

}
