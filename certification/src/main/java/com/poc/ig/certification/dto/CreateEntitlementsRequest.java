package com.poc.ig.certification.dto;

import java.io.Serializable;
import java.util.List;

import com.poc.ig.certification.service.EntitlementType;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
public class CreateEntitlementsRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private EntitlementType entitlementType;
	List<EntitlementRequestDto> entitlements;
	

}
