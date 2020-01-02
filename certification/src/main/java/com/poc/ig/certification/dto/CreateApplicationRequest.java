package com.poc.ig.certification.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poc.ig.certification.entity.Application;

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
	private String certificationName;


	@JsonIgnore
	public Application getApplication() {
		Application app = new Application();
		app.setExternalId(externalId);
		app.setName(name);
		app.setDescription(description);
		return app;
	}

}
