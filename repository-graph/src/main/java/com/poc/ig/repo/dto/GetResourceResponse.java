package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.poc.ig.repo.entity.Resource;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class GetResourceResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private ResourceResponse resource;

	public GetResourceResponse(Resource resourceEntitiy) {
		this.resource = new ResourceResponse(resourceEntitiy);
	}
}
