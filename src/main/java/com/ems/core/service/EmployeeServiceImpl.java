package com.ems.core.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ems.core.dto.PageResponse;
import com.ems.core.exceptions.EntityDependencyMissingException;
import com.ems.core.exceptions.NullIdUpdateException;
import com.ems.core.exceptions.ResourceNotFoundException;
import com.ems.core.model.Department;
import com.ems.core.model.Employee;
import com.ems.core.repository.DepartmentRepository;
import com.ems.core.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository empRepo;

	@Autowired
	private DepartmentRepository deptRepo;

	@Override
	public List<Employee> findAll() {
		return empRepo.findAll();
	}

	@Override
	public PageResponse<Employee> findAll(Pageable pageable) {
		Page<Employee> page = empRepo.findAll(pageable);

		PageResponse<Employee> pageResponse = PageResponse.<Employee>builder().content(page.getContent())
				.pageNumber(page.getNumber()).pageSize(page.getSize()).totalElements((int) page.getTotalElements())
				.totalPages(page.getTotalPages()).build();
		return pageResponse;
	}

	@Override
	public Employee findById(Long id) {
		Objects.requireNonNull(id,"Employee id is mandatory!");
		return empRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(String.format("No Employee found with id:%s", id)));
	}

	@Override
	public Employee insert(Employee employee) {
		Objects.requireNonNull(employee, "A null object is passed to be inserted!");

		// checks deptId is not null , and department exists ,otherwise throws exception
		if (!isDeptExists(employee.getDepartment(), true)) {
			throw new EntityDependencyMissingException(String.format(
					"Employee not inserted!, No Department with id: %s found!", employee.getDepartment().getDeptId()));
		}
		employee.setEmpId(null);
		return empRepo.save(employee);
	}

	@Override
	public Employee update(Employee employee) {
		Objects.requireNonNull(employee, "A null object is passed to be updated!");
		if (employee.getEmpId() == null) {
			throw new NullIdUpdateException("Employee.empId is mandatory when updating!");
		}
		if (!empRepo.existsById(employee.getEmpId())) {
			throw new ResourceNotFoundException(String.format("No Employee with id: %s found!", employee.getEmpId()));
		}
		// checks deptId is not null , and department exists ,otherwise throws exception
		if (!isDeptExists(employee.getDepartment(), true)) {
			throw new EntityDependencyMissingException(String.format(
					"Employee not updated!, No Department with id: %s found!", employee.getDepartment().getDeptId()));
		}
		return empRepo.save(employee);
	}

	@Override
	public void deleteById(Long id) {
		empRepo.deleteById(id);
	}

	/**
	 * checks if the employee's associated department exist in the datasource this
	 * method is also responsible of null checks over department object Optionally
	 * throws {@link IllegalArgumentException} when null values are found during the
	 * check
	 * 
	 * @param dept           department to check its existence
	 * @param throwException if true throws {@link IllegalArgumentException}
	 *                       indicating to the missing required field
	 * @return true if department is found , false if not found or couldn't extract
	 *         departmnetId
	 */
	private boolean isDeptExists(Department dept, boolean throwException) {
		// extracting deptId
		Long deptId = dept != null ? dept.getDeptId() : null;

		if (deptId == null) {
			// if asked to throw exception on null values
			if (throwException) {
				throw new IllegalArgumentException("required department.deptId is missing!");
			}
			// return false if not asked to throw exception
			return false;
		}
		// At this point null checks are passed and returning whether the department
		// exist or not
		return deptRepo.existsById(deptId);
	}

}
