package com.poc.ig.repo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DeleteResourcesRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<String> resources = new ArrayList<String>();

}
