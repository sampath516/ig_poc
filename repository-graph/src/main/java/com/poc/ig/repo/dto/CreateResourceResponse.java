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
public class CreateResourceResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<ResourceResponse> resources = new ArrayList<ResourceResponse>();

	public CreateResourceResponse(List<Resource> resourceEntities) {
		for (Resource res : resourceEntities) {
			resources.add(new ResourceResponse(res));
		}
	}
}
