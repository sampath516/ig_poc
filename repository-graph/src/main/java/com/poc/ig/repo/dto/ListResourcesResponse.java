package com.poc.ig.repo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.poc.ig.repo.entity.Resource;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ListResourcesResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	List<ResourceResponse> resources = new ArrayList<>();

	public ListResourcesResponse(List<Resource> resourceEntities) {
		for (Resource r : resourceEntities) {
			ResourceResponse resourceDto = new ResourceResponse(r);
			resources.add(resourceDto);
		}
	}

}
