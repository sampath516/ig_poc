package com.poc.ig.repo.service;

import java.util.Optional;

import com.poc.ig.repo.entity.User;
import com.poc.ig.repo.exception.InvalidUserException;
import com.poc.ig.repo.repository.UserRepository;

public class ServicesUtil {
	public static User getUserByTenantNameAndExternalId(UserRepository userRepo, String tenantName,
			String userExternalId) {
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

	public static User findUser(UserRepository userRepo, String tenantName, String userExternalId, int depth) {
		Optional<User> userTemp = userRepo.findByTenantNameAndUserExternalId(tenantName, userExternalId);
		User user = null;
		if (userTemp.isPresent()) {
			user = userRepo.findById(userTemp.get().getId(), depth).get();
		} else {
			throw new InvalidUserException("Invalid User(" + tenantName + ", " + userExternalId + ")");
		}
		return user;
	}
}
