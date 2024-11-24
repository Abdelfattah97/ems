package com.ems.core.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.ems.core.dto.PageResponse;
import com.ems.core.exceptions.EntityDependencyMissingException;
import com.ems.core.exceptions.NullIdUpdateException;
import com.ems.core.exceptions.ResourceNotFoundException;
import com.ems.core.model.Employee;


public interface EmployeeService {

	/**
	 * fetches all employees from database
	 * @return lists of all employees
	 */
	List<Employee> findAll();
	
	/**
	 * Returns the employee if found, otherwise throws ResourceNotFoundException
	 * 
	 * @param id the id of the employee to retrieve , not null
	 * @return returns the employee if found
	 * @throws ResourceNotFoundException if the employee was not found , {@link NullPointerException} if id is null
	 */
	Employee findById(Long id);
	
//	List<Employee> findByDepartment(Department department);
	
	/**
	 * inserts a new Employee <br>note: id is set to null by default when inserting
	 * a new object as it is auto generated
	 * @param employee the employee to be inserted, not null
	 * @return returns the employee after being inserted
	 * @throws NullPointerException if the employee is null , {@link EntityDependencyMissingException} if relationship parent cannot be found , {@link IllegalArgumentException} if required field is missing 
	 */
    Employee insert(Employee employee);
    
    /**
	 * Updates the Employee object that holds the same id as the passed object
	 * 
	 * @param employee the employee to be updated, not null , employee id is mandatory
	 * @return returns the employee after being updated
	 * @throws {@link NullIdUpdateException }, {@link ResourceNotFoundException} ,  {@link EntityDependencyMissingException} if relationship parent cannot be found , {@link IllegalArgumentException} if required field is missing 
	 */
    Employee update(Employee employee);
    
    /**
	 * Deletes the employee by its id
	 * @param id the id of the employee to delete, not null
	 */
    void deleteById(Long id);

	/**
	 * fetches Employees Page depending on the paging options in the {@link Pageable} argument
	 * returnes A {@link PageResponse} Object containing the content and paging meta-data 
	 * 
	 * @param pageable a {@link Pageable} object specifying page size,page number,sorting,options,etc...
	 * @return returns a {@link PageResponse} Object containing the content and pagination meta-data
	 */
	PageResponse<Employee> findAll(Pageable pageable);
    
}
