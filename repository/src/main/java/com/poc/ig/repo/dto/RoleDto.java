package com.poc.ig.repo.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poc.ig.repo.entity.Role;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
public class RoleDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String externalId;
	private String tenant;
	private String name;
	private String description;
	private String owner;
	private String organization;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public RoleDto(Role roleEntity) {
		this.id = roleEntity.getId();
		this.externalId = roleEntity.getExternalId();
		this.tenant = roleEntity.getTenant().getName();
		this.name = roleEntity.getName();
		this.description = roleEntity.getDescription();
		this.owner = roleEntity.getOwner().getUserName();
		this.organization = roleEntity.getOrganization().getName();
		this.createdAt = roleEntity.getCreatedAt();
		this.updatedAt = roleEntity.getUpdatedAt();
	}

	@JsonIgnore
	public Role getRole() {
		Role role = new Role();
		role.setExternalId(externalId);
		role.setName(name);
		role.setDescription(description);
		return role;
	}

}
