package com.poc.ig.certification.events.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private String certificationName;
	private EntitlementType entitlementType;
	private List<Entitlement> entitlements = new ArrayList<>();

	@Data
	@Setter
	@Getter
	@NoArgsConstructor
	public static class Entitlement implements Serializable{
		private static final long serialVersionUID = 1L;
		private PrimaryEntity primaryEntity;
		private SecondaryEntity secondaryEntity;
		public Entitlement(PrimaryEntity primaryEntity, SecondaryEntity secondaryEntity) {
			super();
			this.primaryEntity = primaryEntity;
			this.secondaryEntity = secondaryEntity;
		}
	}
	@Data
	@Setter
	@Getter
	@NoArgsConstructor
	public static class PrimaryEntity implements Serializable{
		private static final long serialVersionUID = 1L;
		private String externalId;
		private Map<String, String> properties = new HashMap<String, String>();
		
		public PrimaryEntity(String externalId) {
			super();
			this.externalId = externalId;
		}	
		
	}
	
	@Data
	@Setter
	@Getter
	@NoArgsConstructor
	public static class SecondaryEntity implements Serializable{
		private static final long serialVersionUID = 1L;
		private String externalId;
		private Map<String, String> properties = new HashMap<String, String>();
		
		public SecondaryEntity(String externalId) {
			super();
			this.externalId = externalId;
		}	
			
	}
}
