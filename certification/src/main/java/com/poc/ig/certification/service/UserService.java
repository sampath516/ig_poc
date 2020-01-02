package com.poc.ig.certification.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.poc.ig.certification.dto.CreateUserRequest;
import com.poc.ig.certification.dto.CreateUserResponse;
import com.poc.ig.certification.dto.UserRequest;
import com.poc.ig.certification.dto.UserResponse;
import com.poc.ig.certification.entity.Certification;
import com.poc.ig.certification.entity.Organization;
import com.poc.ig.certification.entity.User;
import com.poc.ig.certification.repository.CertificationRepository;
import com.poc.ig.certification.repository.OrganizationRepository;
import com.poc.ig.certification.repository.UserRepository;


@RestController
@RequestMapping(value = "ig/certification/v1/tenants/{tenantName}/{certificationName}", produces = "application/json", consumes = "application/json")
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private OrganizationRepository orgRepo;
	
	@Autowired
	private CertificationRepository certRepo;

	@PostMapping(path = "users")
	@ResponseStatus(HttpStatus.CREATED)
	public CreateUserResponse createUser(@PathVariable String tenantName, @PathVariable String certificationName,  @RequestBody CreateUserRequest createUserReq) {
		Certification cert = ServicesUtil.validateCertification(certRepo, tenantName, certificationName);
		List<UserRequest>  usersIn = createUserReq.getUsers();
		List<User> userEntities = new ArrayList<User>();
		for(UserRequest userIn : usersIn) {
			Organization org = ServicesUtil.validateOrganization(orgRepo, tenantName, userIn.getOrganization());
			User user = new User();
			user.setExternalId(userIn.getExternalId());
			user.setName(userIn.getName());
			user.setFirstName(userIn.getFirstName());
			user.setLastName(userIn.getLastName());
			user.setTenant(org.getTenant());
			user.setTenantName(tenantName);
			user.setCertificationName(cert.getName());
			org.getUsers().add(user);
			user.setOrganization(org);
			if(userIn.getManager() != null && !userIn.getManager().trim().isEmpty()) {
				user.setManager(ServicesUtil.validateUser(userRepo, tenantName, userIn.getManager()));	
			}			
			user = userRepo.save(user);
			userEntities.add(user);
		}
		return new CreateUserResponse(userEntities);
	}

	@GetMapping(path = "users/{userExternalId}")
	public ResponseEntity<UserResponse> getUser(@PathVariable String tenantName, @PathVariable String certificationName,
			@PathVariable String userExternalId) {
		User user = ServicesUtil.getUserByTenantNameAndExternalId(userRepo, tenantName, userExternalId);
		return new ResponseEntity<>(new UserResponse(user), HttpStatus.OK);
	}

	@DeleteMapping(path = "users/{userExternalId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable String tenantName, @PathVariable String certificationName, @PathVariable String userExternalId) {
		userRepo.delete(ServicesUtil.validateUser(userRepo, tenantName, userExternalId));
	}
}
