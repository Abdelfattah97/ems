package com.ems.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.core.model.AppRole;


public interface AppRoleRepository extends JpaRepository<AppRole, Long>{

	Optional<AppRole> findByRoleName(String role_name);
	
	
}
