package com.poc.ig.connector.im.dto;

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
public class UserResourceEntitlements {

	private String tenantName;
	private String organizationName;
	private String certificationName;
	private List<UserResourceEntitlement> entitlements = new ArrayList<UserResourceEntitlement>();

}
