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
public class UserResourceEntitlementRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private String tenantName;
	private String certification;
	private List<UserResourceEntitlement> entitlements = new ArrayList<UserResourceEntitlement>();

}
