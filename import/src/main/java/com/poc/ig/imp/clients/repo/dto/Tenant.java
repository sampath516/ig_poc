package com.poc.ig.imp.clients.repo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
public class Tenant implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
