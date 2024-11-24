package com.ems.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ems.core.dto.PageResponse;
import com.ems.core.model.Department;
import com.ems.core.service.DepartmentService;
import com.ems.core.service.pagination.PageRequestFactory;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

	@Autowired
	DepartmentService departmentService;

	@GetMapping("/paged")
	public PageResponse<Department> findAllPageable(@RequestParam(required = false) Integer pgNum, @RequestParam(required = false) Integer pgSize, Direction dir,
			@RequestParam(defaultValue = "deptId") String[] sortBy) {
		Pageable pageable = PageRequestFactory.createPageRequest(pgSize, pgNum, dir, sortBy);
		return departmentService.findAll(pageable);
	}

	@GetMapping("/{deptId}")
	public Department findById(@PathVariable Long deptId) {
		return departmentService.findById(deptId);
	}

	@PostMapping
	public Department insert(@RequestBody Department dept) {
		return departmentService.insert(dept);
	}

	@PutMapping
	public Department update(@RequestBody Department dept) {
		return departmentService.update(dept);
	}

	@DeleteMapping("/{deptId}")
	public void deleteById(@PathVariable Long deptId) {
		departmentService.deleteById(deptId);
	}

}
