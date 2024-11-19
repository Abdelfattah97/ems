package com.ems.core.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ems.core.dto.PageResponse;
import com.ems.core.exceptions.NullIdUpdateException;
import com.ems.core.exceptions.ResourceNotFoundException;
import com.ems.core.model.Department;

@Service
public interface DepartmentService {

	/**
	 * fetches Employees Page depending on the paging options in the
	 * {@link Pageable} argument returnes A {@link PageResponse} Object containing
	 * the content and paging meta-data
	 * 
	 * @param pageable a {@link Pageable} object specifying page size,page
	 *                 number,sorting,options,etc...
	 * @return returns a {@link PageResponse} Object containing the content and
	 *         pagination meta-data
	 */
	PageResponse<Department> findAll(Pageable pageable);

	/**
	 * fetches all departments from database
	 * 
	 * @return lists of all departments
	 */
	List<Department> findAll();

	/**
	 * Returns the department if found, otherwise throws ResourceNotFoundException
	 * 
	 * @param id the id of the department to retrieve , not null
	 * @return returns the department if found
	 * @throws ResourceNotFoundException if the department was not found
	 */
	Department findById(Long id);

	/**
	 * Returns the department if found, otherwise throws ResourceNotFoundException
	 * 
	 * @param name the name of the department to retrieve , not null
	 * @return returns the department if found
	 * @throws ResourceNotFoundException if the department was not found
	 */
	Department findByName(String name);

	/**
	 * inserts a new Department , note: id is set to null by default when inserting
	 * a new object as it is auto generated
	 * 
	 * @param department the department to be inserted, not null
	 * @return returns the department after being inserted
	 *  @throws NullPointerException if the employee is null 
	 */
	Department insert(Department department);

	/**
	 * Updates the Department object that holds the same id as the passed object
	 * 
	 * @param department the department to be updated, not null , department id is
	 *                   mandatory
	 * @return returns the department after being updated
	 * @throws NullIdUpdateException , ResourceNotFoundException 
	 */
	Department update(Department department);

	/**
	 * Deletes the department by its id
	 * 
	 * @param id the id of the department to delete, not null
	 */
	void deleteById(Long id);

}
