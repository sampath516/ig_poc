package com.poc.ig.imp.clients.repo.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class TenantList implements Serializable{
	private static final long serialVersionUID = 1L;
	List<Tenant> tenants;
}
