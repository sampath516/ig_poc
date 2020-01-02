package com.poc.ig.certification.test.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
public class GetReviewsByCertificationAndReviewerResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String tenant;
	private String certification;
	private String reviewer;
	private int total;
	List<ReviewResponse> reviews = new ArrayList<ReviewResponse>();

}
