package com.poc.ig.certification.dto;

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
public class ApplicationResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String externalId;
	private String name;
	private String description;
	private String owner;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
