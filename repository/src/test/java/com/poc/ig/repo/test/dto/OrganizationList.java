package com.poc.ig.repo.test.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OrganizationList implements Serializable {
	List<Organization> organizations;
}
