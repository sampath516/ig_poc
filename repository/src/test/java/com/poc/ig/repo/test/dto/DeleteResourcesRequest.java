package com.poc.ig.repo.test.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
public class DeleteResourcesRequest {
	
	private List<String> resources = new ArrayList<String>();

}
