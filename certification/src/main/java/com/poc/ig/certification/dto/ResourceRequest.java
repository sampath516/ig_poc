package com.poc.ig.certification.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ResourceRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String externalId;
	private String name;
	private String description;
	private String owner;
	private String application;
}
