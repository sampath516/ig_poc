package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.poc.ig.repo.entity.Organization;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class GetOrganizationResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private OrganizationResponse organizaion;

	public GetOrganizationResponse(Organization org) {
		this.organizaion = new OrganizationResponse(org);
	}
}
