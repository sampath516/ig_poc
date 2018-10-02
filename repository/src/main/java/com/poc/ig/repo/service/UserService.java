package com.poc.ig.repo.service;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.poc.ig.repo.dto.CreateUserRequest;
import com.poc.ig.repo.dto.CreateUserResponse;
import com.poc.ig.repo.dto.GetUserResponse;
import com.poc.ig.repo.dto.LinkResourcesResponse;
import com.poc.ig.repo.dto.LinkRolesResponse;
import com.poc.ig.repo.dto.ListUsersResponse;
import com.poc.ig.repo.dto.UnLinkResourcesResponse;
import com.poc.ig.repo.dto.UpdateUserRequest;
import com.poc.ig.repo.dto.UpdateUserResponse;
import com.poc.ig.repo.entity.Organization;
import com.poc.ig.repo.entity.Resource;
import com.poc.ig.repo.entity.Role;
import com.poc.ig.repo.entity.User;
import com.poc.ig.repo.exception.InvalidOrganizationIdException;
import com.poc.ig.repo.exception.InvalidResourceIdException;
import com.poc.ig.repo.exception.InvalidRoleIdException;
import com.poc.ig.repo.exception.InvalidUserIdException;
import com.poc.ig.repo.repository.OrganizationRepository;
import com.poc.ig.repo.repository.ResourceRepository;
import com.poc.ig.repo.repository.RoleRepository;
import com.poc.ig.repo.repository.UserRepository;

@RestController
@RequestMapping(value = "ig/repo/v1/{tenantId}/", produces = "application/json", consumes = "application/json")
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private OrganizationRepository orgRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private ResourceRepository resourceRepo;

	@PostMapping(path = "{orgId}/users")
	@ResponseStatus(HttpStatus.CREATED)
	public CreateUserResponse createUser(@PathVariable long tenantId, @PathVariable long orgId,
			@RequestBody CreateUserRequest createUserReq) {
		Organization org = validateTenantOrganization(tenantId, orgId);
		User user = createUserReq.getUser();
		user.setOrganization(org);
		return new CreateUserResponse(userRepo.save(user));
	}

	@GetMapping(path = "{orgId}/users/{userId}")
	public ResponseEntity<GetUserResponse> getUser(@PathVariable long tenantId, @PathVariable long orgId,
			@PathVariable long userId) {
		User user = validateUser(tenantId, orgId, userId);
		return new ResponseEntity<>(new GetUserResponse(user), HttpStatus.OK);
	}

	@GetMapping(path = "{orgId}/users")
	@ResponseStatus(HttpStatus.OK)
	public ListUsersResponse listUsers(@PathVariable long tenantId, @PathVariable long orgId) {
		Organization org = validateTenantOrganization(tenantId, orgId);
		return new ListUsersResponse(org.getUsers());
	}

	@PutMapping(path = "{orgId}/users")
	public ResponseEntity<UpdateUserResponse> updateUser(@PathVariable long tenantId, @PathVariable long orgId,
			@RequestBody UpdateUserRequest updateUserReq, @RequestParam boolean basicUpdate) {
		User user = validateUser(tenantId, orgId, updateUserReq.getId());
		user = updateUserReq.getUpdatedUser(user, basicUpdate);
		return new ResponseEntity<>(new UpdateUserResponse(userRepo.save(user)), HttpStatus.OK);
	}

	@DeleteMapping(path = "{orgId}/users/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable long tenantId, @PathVariable long orgId, @PathVariable long userId) {
		userRepo.delete(validateUser(tenantId, orgId, userId));
	}

	@PutMapping(path = "{orgId}/users/{userId}/roles")
	public ResponseEntity<LinkRolesResponse> linkRoles(@PathVariable long tenantId, @PathVariable long orgId,
			@PathVariable long userId, List<Long> roleIds) {
		User user = validateUser(tenantId, orgId, userId);
		List<Role> roles = validateRoles(tenantId, orgId, roleIds);
		for (Role r : roles) {
			user.getRoles().add(r);
			r.getUsers().add(user);
			roleRepo.save(r);
		}
		return new ResponseEntity<>(new LinkRolesResponse(userRepo.save(user)), HttpStatus.OK);
	}

	@DeleteMapping(path = "{orgId}/users/{userId}/roles")
	public ResponseEntity<LinkRolesResponse> unlinkRoles(@PathVariable long tenantId, @PathVariable long orgId,
			@PathVariable long userId, List<Long> roleIds) {
		User user = validateUser(tenantId, orgId, userId);
		List<Role> roles = validateRoles(tenantId, orgId, roleIds);
		for (Role r : roles) {
			user.getRoles().remove(r);
			r.getUsers().remove(user);
			roleRepo.save(r);
		}
		return new ResponseEntity<>(new LinkRolesResponse(userRepo.save(user)), HttpStatus.OK);
	}

	@PutMapping(path = "{orgId}/users/{userId}/resources")
	public ResponseEntity<LinkResourcesResponse> linkResources(@PathVariable long tenantId,
			@PathVariable long orgId, @PathVariable long userId, List<Long> resourceIds) {
		User user = validateUser(tenantId, orgId, userId);
		List<Resource> resources = validateResources(tenantId, orgId, resourceIds);
		for (Resource r : resources) {
			user.getResources().add(r);
			r.getUsers().add(user);
			resourceRepo.save(r);
		}
		return new ResponseEntity<>(new LinkResourcesResponse(userRepo.save(user)), HttpStatus.OK);
	}

	@DeleteMapping(path = "{orgId}/users/{userId}/resources")
	public ResponseEntity<UnLinkResourcesResponse> unLinkResources(@PathVariable long tenantId,
			@PathVariable long orgId, @PathVariable long userId, List<Long> resourceIds) {
		User user = validateUser(tenantId, orgId, userId);
		List<Resource> resources = validateResources(tenantId, orgId, resourceIds);
		for (Resource r : resources) {
			user.getResources().remove(r);
			r.getUsers().remove(user);
			resourceRepo.save(r);
		}
		return new ResponseEntity<>(new UnLinkResourcesResponse(userRepo.save(user)), HttpStatus.OK);
	}

	private Organization validateTenantOrganization(long tenantId, long orgId) {
		Organization org = orgRepo.findByTenantIdAndOrgId(tenantId, orgId);
		if (org != null) {
			return org;
		} else {
			throw new InvalidOrganizationIdException(
					"Organization Id, " + orgId + ", does not belong to the tenant Id " + tenantId);
		}
	}

	private User validateUser(long tenantId, long orgId, long userId) {
		validateTenantOrganization(tenantId, orgId);
		User user = userRepo.findByOrgIdAndUserId(orgId, userId);
		if (user != null) {
			return user;
		} else {
			throw new InvalidUserIdException("User does not exist: " + userId);
		}
	}

	private List<Role> validateRoles(long tenantId, long orgId, List<Long> roleIds) {
		validateTenantOrganization(tenantId, orgId);
		List<Role> roles = new ArrayList<>();
		for (Long roleId : roleIds) {
			Role role = roleRepo.findByOrgIdAndRoleId(orgId, roleId);
			if (role == null) {
				throw new InvalidRoleIdException("Role does not exist: " + roleId);
			}
			roles.add(role);
		}
		return roles;
	}

	private List<Resource> validateResources(long tenantId, long orgId, List<Long> resourceIds) {
		validateTenantOrganization(tenantId, orgId);
		List<Resource> resources = new ArrayList<>();
		for (Long resourceId : resourceIds) {
			Resource resource = resourceRepo.findByOrgIdAndResourceId(orgId, resourceId);
			if (resource == null) {
				throw new InvalidResourceIdException("Resource does not exist: " + resourceId);
			}
			resources.add(resource);
		}
		return resources;
	}
}
