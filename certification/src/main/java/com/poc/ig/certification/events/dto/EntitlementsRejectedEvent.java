package com.poc.ig.certification.events.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.poc.ig.certification.entity.Entitlement.EntitlementType;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
public class EntitlementsRejectedEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String tenant;
	private EntitlementType entitlementType;
	private List<Entitlement> entitlements = new ArrayList<>();

	@Data
	@Setter
	@Getter
	@NoArgsConstructor
	public static class Entitlement implements Serializable{
		private static final long serialVersionUID = 1L;
		
		public Entitlement(String primaryEntityExternalId, String secondaryEntityExternalId) {  
			super();
			this.primaryEntityExternalId = primaryEntityExternalId;
			this.secondaryEntityExternalId = secondaryEntityExternalId;
		}
		private String primaryEntityExternalId;
		private String secondaryEntityExternalId;

	}
}
