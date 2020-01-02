package com.poc.ig.certification.dto;

import java.io.Serializable;

import com.poc.ig.certification.entity.Review.ReviewState;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Setter
@Getter
@NoArgsConstructor
public class GetReviewsByCertificationAndReviewerRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private ReviewState reviewState;

}
