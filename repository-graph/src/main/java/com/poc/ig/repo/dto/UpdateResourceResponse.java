package com.poc.ig.repo.dto;

import java.io.Serializable;

import com.poc.ig.repo.entity.Resource;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UpdateResourceResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private ResourceResponse resource;

	public UpdateResourceResponse(Resource resourceEntity) {
		this.resource = new ResourceResponse(resourceEntity);
	}

}
