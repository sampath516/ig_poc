package com.poc.ig.repo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.poc.ig.repo.dto.CreateUserRequest;
import com.poc.ig.repo.dto.CreateUserResponse;
import com.poc.ig.repo.dto.DeleteUsersRequest;
import com.poc.ig.repo.dto.ListUsersResponse;
import com.poc.ig.repo.dto.UpdateUserRequest;
import com.poc.ig.repo.dto.UpdateUserResponse;
import com.poc.ig.repo.dto.UserRequest;
import com.poc.ig.repo.dto.UserResponse;
import com.poc.ig.repo.entity.Organization;
import com.poc.ig.repo.entity.Resource;
import com.poc.ig.repo.entity.Role;
import com.poc.ig.repo.entity.Tenant;
import com.poc.ig.repo.entity.User;
import com.poc.ig.repo.exception.InvalidOrganizationException;
import com.poc.ig.repo.exception.InvalidResourceException;
import com.poc.ig.repo.exception.InvalidRoleException;
import com.poc.ig.repo.exception.InvalidTenantException;
import com.poc.ig.repo.exception.InvalidUserException;
import com.poc.ig.repo.repository.OrganizationRepository;
import com.poc.ig.repo.repository.ResourceRepository;
import com.poc.ig.repo.repository.RoleRepository;
import com.poc.ig.repo.repository.TenantRepository;
import com.poc.ig.repo.repository.UserRepository;

@RestController
@RequestMapping(value = "ig/repo/v1/tenants/{tenantName}/", produces = "application/json", consumes = "application/json")
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private OrganizationRepository orgRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private ResourceRepository resourceRepo;

	@Autowired
	private TenantRepository tenantRepo;

	@PostMapping(path = "users")
	@ResponseStatus(HttpStatus.CREATED)
	public CreateUserResponse createUser(@PathVariable String tenantName, @RequestBody CreateUserRequest createUserReq) {
		List<UserRequest>  usersIn = createUserReq.getUsers();
		List<User> userEntities = new ArrayList<User>();
		for(UserRequest userIn : usersIn) {
			Organization org = validateOrganization(tenantName, userIn.getOrganization());
			User user = new User();
			user.setExternalId(userIn.getExternalId());
			user.setUserName(userIn.getName());
			user.setFirstName(userIn.getFirstName());
			user.setLastName(userIn.getLastName());
			user.setTenant(org.getTenant());
			org.getUsers().add(user);
			user.setOrganization(org);
			if(userIn.getManager() != null && !userIn.getManager().trim().isEmpty()) {
				user.setManager(validateUser(tenantName, userIn.getManager()));	
			}			
			user = userRepo.save(user);
			userEntities.add(user);
		}
		return new CreateUserResponse(userEntities);
	}

	@GetMapping(path = "users/{userExternalId}")
	public ResponseEntity<UserResponse> getUser(@PathVariable String tenantName,
			@PathVariable String userExternalId) {
		User user = getUserByTenantNameAndExternalId(tenantName, userExternalId);
		return new ResponseEntity<>(new UserResponse(user), HttpStatus.OK);
	}

	@GetMapping(path = "users")
	@ResponseStatus(HttpStatus.OK)
	public ListUsersResponse listUsers(@PathVariable String tenantName) {
		validateTenant(tenantName);
		return new ListUsersResponse(userRepo.findByTenantName(tenantName));
	}

	@PutMapping(path = "users/{userExternalId}")
	public ResponseEntity<UpdateUserResponse> updateUser(@PathVariable String tenantName,
			@PathVariable String userExternalId, @RequestBody UpdateUserRequest updateUserReq) {
		User user = validateUser(tenantName, userExternalId);
		user.setUserName(updateUserReq.getUserName());
		user.setFirstName(updateUserReq.getFirstName());
		user.setLastName(updateUserReq.getLastName());
		user.setEmail(updateUserReq.getEmail());
		if (!user.getOrganization().getExternalId().equals(updateUserReq.getOrganization())) {
			user.setOrganization(validateOrganization(tenantName, updateUserReq.getOrganization()));
		}
		if (!user.getManager().getExternalId().equals(updateUserReq.getManager())) {
			user.setManager(validateUser(tenantName, updateUserReq.getManager()));
		}
		user = updateUserReq.getUpdatedUser(user);
		return new ResponseEntity<>(new UpdateUserResponse(userRepo.save(user)), HttpStatus.OK);
	}

	@DeleteMapping(path = "users/{userExternalId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable String tenantName, @PathVariable String userExternalId) {
		userRepo.delete(validateUser(tenantName, userExternalId));
	}
	
	@DeleteMapping(path = "users")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUsers(@PathVariable String tenantName, @RequestBody DeleteUsersRequest deleteUserRequest) {
		for(String userExternalId: deleteUserRequest.getUsers()) {
			userRepo.delete(validateUser(tenantName, userExternalId));
		}		
	}

	@PutMapping(path = "users/{userExternalId}/roles")
	public ResponseEntity<UserResponse> linkRoles(@PathVariable String tenantName, @PathVariable String userExternalId, @RequestBody List<String> roles) {
		User user = validateUser(tenantName, userExternalId);

		for (String role : roles) {
			Role roleEntity = validateRole(tenantName, role);
			user.getRoles().add(roleEntity);
			roleEntity.getUsers().add(user);
			roleRepo.save(roleEntity);
		}
		return new ResponseEntity<UserResponse>(new UserResponse(userRepo.save(user)), HttpStatus.OK);
	}
	
	@DeleteMapping(path = "users/{userExternalId}/roles")
	@ResponseStatus(HttpStatus.OK)
	public void unlinkRoles(@PathVariable String tenantName, @PathVariable String userExternalId, @RequestBody List<String> roles) {
		User user = getUserByTenantNameAndExternalId(tenantName, userExternalId);
		for (String role : roles) {			
			Role roleEntity = loadRole(role);
			if(roleEntity != null) {
				user.getRoles().remove(roleEntity);
				roleEntity.getUsers().remove(user);
				roleRepo.save(roleEntity);
			}			
		}
		userRepo.save(user);
	}

	@PutMapping(path = "users/{userExternalId}/resources")
	public ResponseEntity<UserResponse> linkResources(@PathVariable String tenantName, @PathVariable String userExternalId, @RequestBody List<String> resources) {
		User user = validateUser(tenantName, userExternalId);

		for (String res : resources) {
			Resource resEntity = validateResource(tenantName, res);
			user.getResources().add(resEntity);
			resEntity.getUsers().add(user);
			resourceRepo.save(resEntity);
		}
		return new ResponseEntity<>(new UserResponse(userRepo.save(user)), HttpStatus.OK);
	}

	@DeleteMapping(path = "users/{userExternalId}/resources")
	@ResponseStatus(HttpStatus.OK)
	public void unLinkResources(@PathVariable String tenantName, @PathVariable String userExternalId, @RequestBody List<String> resources) {
		User user = getUserByTenantNameAndExternalId(tenantName, userExternalId);
      	for (String res : resources) {			
			Resource resourceEntity = loadResource(res);
			if (resourceEntity != null) {
				user.getResources().remove(resourceEntity);
				resourceEntity.getUsers().remove(user);
				resourceRepo.save(resourceEntity);
			}			
		}
		userRepo.save(user);
	}

	private Organization validateOrganization(String tenantName, String orgExternalId) {
		Optional<Organization> org = orgRepo.findByTenantNameAndOrgExternalId(tenantName, orgExternalId);

		if (org.isPresent()) {
			return org.get();
		} else {
			throw new InvalidOrganizationException("Invalid Organizatio(" + tenantName + ", " + orgExternalId + ")");
		}
	}

	private User validateUser(String tenantName, String userExternalId) {
		Optional<User> user = userRepo.findByTenantNameAndUserExternalId(tenantName, userExternalId);
		if (user.isPresent()) {
			return user.get();
		} else {
			throw new InvalidUserException("Invalid User(" + tenantName + ", " + userExternalId + ")");
		}
	}

	private Tenant validateTenant(String tenantName) {
		Optional<Tenant> tenant = tenantRepo.findByName(tenantName);
		if (tenant.isPresent()) {
			return tenant.get();
		} else {
			throw new InvalidTenantException("Invalid Tenant: " + tenantName);
		}

	}

	private Role validateRole(String tenantName, String roleExternalId) {
		Optional<Role> role = roleRepo.findByTenantNameAndRoleExternalId(tenantName, roleExternalId);
		if (role.isPresent()) {
			return role.get();
		} else {
			throw new InvalidRoleException("Invalid Role(" + tenantName + ", " + roleExternalId + ")");
		}
	}
	
	private Role loadRole(String roleExternalId) {
		Optional<Role> role = roleRepo.findByExternalId(roleExternalId);
		if (role.isPresent()) {
			return role.get();
		} else {
			return null;
		}
	}

	private Resource validateResource(String tenantName, String resourceExternalId) {
		Optional<Resource> resource = resourceRepo.findByTenantNameAndResourceExternalId(tenantName,
				resourceExternalId);
		if (resource.isPresent()) {
			return resource.get();
		} else {
			throw new InvalidResourceException("Invalid Resource(" + tenantName + ", " + resourceExternalId + ")");
		}
	}
	
	private Resource loadResource(String resourceExternalId) {
		Optional<Resource> resource = resourceRepo.findByExternalId(resourceExternalId);
		if (resource.isPresent()) {
			return resource.get();
		} else {
			return null;
		}
	}
	
	private User getUserByTenantNameAndExternalId(String tenantName, String userExternalId) {
		Optional<User> userTemp = userRepo.findByExternalId(userExternalId);
		User user = null;
		if (userTemp.isPresent()) {
			user = userTemp.get();
			if (!user.getTenant().getName().equals(tenantName)) {
				throw new InvalidUserException("Invalid User(" + tenantName + ", " + userExternalId + ")");
			}
		} else {
			throw new InvalidUserException("Invalid User(" + tenantName + ", " + userExternalId + ")");
		}
		return user;
	}
}
