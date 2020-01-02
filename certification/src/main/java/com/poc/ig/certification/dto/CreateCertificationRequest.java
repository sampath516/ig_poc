package com.poc.ig.certification.dto;

import java.io.Serializable;

import com.poc.ig.certification.entity.Certification.CertificationType;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateCertificationRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private String tenantName;
	//Organization ExternalID
	private String organization;
	// User ExternalID
	private String owner;
	private CertificationType certificationType;
	
}
