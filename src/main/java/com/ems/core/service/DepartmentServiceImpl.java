package com.ems.core.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ems.core.dto.PageResponse;
import com.ems.core.exceptions.NullIdUpdateException;
import com.ems.core.exceptions.ResourceNotFoundException;
import com.ems.core.model.Department;
import com.ems.core.repository.DepartmentRepository;

@Service
public class DepartmentServiceImpl implements DepartmentService{

	@Autowired 
	private DepartmentRepository deptRepo;	
//	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public PageResponse<Department> findAll(Pageable pageable) {
		Page<Department> page = deptRepo.findAll(pageable);
		return PageResponse.<Department>builder()
				.content(page.getContent())
				.pageNumber(page.getNumber())
				.pageSize(page.getSize())
				.totalElements((int)page.getTotalElements())
				.totalPages(page.getTotalPages())
				.build();
	}

	@Override
	public Department findById(Long id) {
		return deptRepo.findById(id).orElseThrow(()->new ResourceNotFoundException(String.format("No Department found with id:%s", id)));
	}

	@Override
	public Department insert(Department department) {
		Objects.requireNonNull(department, "A null object is passed to be inserted!");
		department.setDeptId(null);
		return deptRepo.save(department);
	}

	@Override
	public Department update(Department department) {
		
		Objects.requireNonNull(department,"A null object is passed to be updated!");
		Long empId = department.getDeptId();
		
		if(empId == null) {
            throw new NullIdUpdateException("The department id must be provided to update a department!");
        }
		return deptRepo.save(department);
	}

	@Override
	public void deleteById(Long id) {
		deptRepo.deleteById(id);
	}

	@Override
	public Department findByName(String name) {
		return deptRepo.findByDeptName(name).orElseThrow(()->new ResourceNotFoundException(String.format("No department with name:%s is found!",name)));
	}

	@Override
	public List<Department> findAll() {
		return deptRepo.findAll();
	}
	
	
}
