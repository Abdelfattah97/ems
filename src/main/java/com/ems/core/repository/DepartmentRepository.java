package com.ems.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.core.model.Department;


public interface DepartmentRepository extends JpaRepository<Department, Long> {

	Optional<Department> findByDeptName(String deptName);
	
	
}
