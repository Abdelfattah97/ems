package com.ems.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.core.model.Department;
import com.ems.core.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	public List<Employee> findByDepartment (Department department);
	
}
