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

import com.ems.core.dto.EmployeeDto;
import com.ems.core.dto.PageResponse;
import com.ems.core.dto.mapper.EmployeeDtoMapper;
import com.ems.core.dto.mapper.PageResponseDtoMapper;
import com.ems.core.model.Employee;
import com.ems.core.service.EmployeeService;
import com.ems.core.service.pagination.PageRequestFactory;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	EmployeeDtoMapper empDtoMapper;

	@Autowired
	PageResponseDtoMapper<Employee, EmployeeDto> pageMapper;

	@GetMapping("/paged")
	public PageResponse<EmployeeDto> findAllPageable(@RequestParam(required = false) Integer pageNum,@RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Direction dir, @RequestParam(required = false) String[] sortBy) {
		Pageable pageable = PageRequestFactory.createPageRequest(pageSize, pageNum, dir, sortBy);
		return pageMapper.toDto(employeeService.findAll(pageable), e -> empDtoMapper.toDto(e));
	}

	@GetMapping("/{empId}")
	public EmployeeDto findById(@PathVariable Long empId) {
		return empDtoMapper.toDto(employeeService.findById(empId));
	}

	@PostMapping()
	public EmployeeDto insert(@Valid @RequestBody EmployeeDto emp) {

		return empDtoMapper.toDto(employeeService.insert(empDtoMapper.toEntity(emp)));
	}

	@PutMapping
	public EmployeeDto update(@Valid @RequestBody EmployeeDto emp) {
		return empDtoMapper.toDto(employeeService.update(empDtoMapper.toEntity(emp)));
	}

	@DeleteMapping("/{empId}")
	public void deleteById(@PathVariable Long empId) {
		employeeService.deleteById(empId);
	}

}
