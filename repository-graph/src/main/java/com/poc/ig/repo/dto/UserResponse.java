package com.poc.ig.repo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.poc.ig.repo.entity.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String externalId;
	private String tenantName;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private String manager;
	private String organization;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public UserResponse(User user) {
		this.id = user.getId();
		this.externalId = user.getExternalId();
		this.tenantName = user.getTenant().getName();
		this.userName = user.getUserName();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		if(user.getManager() != null) {
			this.manager = user.getManager().getUserName();	
		}		
		this.organization = user.getOrganization().getName();
		this.createdAt = user.getCreatedAt();
		this.updatedAt = user.getUpdatedAt();
	}

}
