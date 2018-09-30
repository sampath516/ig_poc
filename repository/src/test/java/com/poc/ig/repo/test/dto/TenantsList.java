package com.poc.ig.repo.test.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TenantsList implements Serializable {
	private static final long serialVersionUID = 1L;
	List<Tenant> tenants;

}
