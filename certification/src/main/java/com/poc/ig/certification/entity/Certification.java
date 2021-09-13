package com.poc.ig.certification.entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@NodeEntity(label = "certification")
public class Certification  extends AbstractEntity  {

	private CertificationState state = CertificationState.CREATE;
	private CertificationType type;

	@Relationship(type = "CERT_BELONGS_TO_TEN", direction = Relationship.OUTGOING)
	private Tenant tenant;

	@Relationship(type = "CERT_BELONGS_TO_ORG", direction = Relationship.OUTGOING)
	private Organization organization;
	
	@Relationship(type = "OWNER_OF_CERT", direction = Relationship.INCOMING)
	private User owner;

	@Relationship(type = "CERT_HAS_ENTITLEMENT", direction = Relationship.OUTGOING)
	private Set<Entitlement> entitlements = new HashSet<Entitlement>();
	
	@Relationship(type = "CERT_ASSOCIATED_REVIEW_LOGIC", direction = Relationship.OUTGOING)
	private CertificationReviewLogic reviewLogic;
	
	@Relationship(type = "REVIEWER_OF_CERT", direction = Relationship.INCOMING)
	private Set<User> reviewers = new HashSet<User>();

	public static enum CertificationState {
		CREATE(1), IN_PROGRESS(2), STOPPED(3), COMPLETED(4);
		private int value;

		CertificationState(int state) {
			this.value = state;
		}

		public int getValue() {
			return value;
		}

		public static CertificationState fromValue(int value) {
			for (CertificationState v : values()) {
				if (v.value == value) {
					return v;
				}
			}
			throw new IllegalArgumentException(
					"Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
		}

	}

	public static enum CertificationType { 
		USER_PREVILEGES(1), ROLE(2), RESOURCE(3), ACCOUNT(4);

		private int value;

		private CertificationType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static CertificationType fromValue(int value) {
			for (CertificationType v : values()) {
				if (v.value == value) {
					return v;
				}
			}
			throw new IllegalArgumentException(
					"Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
		}

	}

}
