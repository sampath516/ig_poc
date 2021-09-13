package com.poc.ig.connector.im.events.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
public class EntitlementsPublishedEventDto implements Serializable {
	private static final long serialVersionUID = 1L; 
	private String tenantName;
	private String certificationName;
	private int entitlementsCount;

}
