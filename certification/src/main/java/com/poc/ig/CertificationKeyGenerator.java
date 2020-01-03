package com.poc.ig;

import org.neo4j.ogm.id.IdStrategy;

import com.poc.ig.certification.entity.AbstractEntity;
import com.poc.ig.certification.entity.Certification;
import com.poc.ig.certification.entity.Organization;
import com.poc.ig.certification.entity.Review;
import com.poc.ig.certification.entity.Tenant;
import com.poc.ig.certification.entity.UserPrivilegesResourceEntitlement;
import com.poc.ig.certification.exception.InvalidEntityException;

public class CertificationKeyGenerator implements IdStrategy {

	@Override
	public Object generateId(Object entity) {
		String key = null;

		if (entity instanceof AbstractEntity) {

			AbstractEntity absEntity = (AbstractEntity) entity;
			if (entity instanceof Tenant) {
				key = absEntity.getName();
			} else if (entity instanceof Organization || entity instanceof Certification) {
				key = absEntity.getTenantName() + absEntity.getName();
			} else if (entity instanceof Review) {
				key = ((Review) entity).getEntitlement().getKey() + ((Review) entity).getReviewer().getExternalId();
			} else if (entity instanceof UserPrivilegesResourceEntitlement) {
				key = absEntity.getTenantName() + absEntity.getCertificationName()
						+ ((UserPrivilegesResourceEntitlement) absEntity).getPrimaryEntity().getExternalId()
						+ ((UserPrivilegesResourceEntitlement) absEntity).getSecondaryEntity().getExternalId();
			}

			else {
				key = absEntity.getTenantName() + absEntity.getCertificationName() + absEntity.getExternalId();
			}

			return key.trim().toLowerCase();
		}
		throw new InvalidEntityException("Invalid Entity Type: " + entity.getClass());
	}

}
