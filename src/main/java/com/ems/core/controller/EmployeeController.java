package com.ems.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
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

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employees")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	EmployeeDtoMapper empDtoMapper;

	@Autowired
	PageResponseDtoMapper<Employee, EmployeeDto> pageMapper;

	@GetMapping("/paged")
	@Operation(summary = "fetches employees' pages", responses = {
			@ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "401", description = "Invalid Bearer Token!"),
			@ApiResponse(responseCode = "403", description = "Don't have permission to access this resource")

	})
	public PageResponse<EmployeeDto> findAllPageable(@RequestParam(required = false) Integer pageNum,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Direction dir,
			@RequestParam(required = false) String[] sortBy) {
		Pageable pageable = PageRequestFactory.createPageRequest(pageSize, pageNum, dir, sortBy);
		return pageMapper.toDto(employeeService.findAll(pageable), e -> empDtoMapper.toDto(e));
	}

	@Operation(summary = "fetches employee by its id", responses = {
			@ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "Employee Not Found!"),
			@ApiResponse(responseCode = "401", description = "Invalid Bearer Token!"),
			@ApiResponse(responseCode = "403", description = "Don't have permission to access this resource")

	})
	@GetMapping("/{empId}")
	public EmployeeDto findById(@NotNull @PathVariable Long empId) {
		return empDtoMapper.toDto(employeeService.findById(empId));
	}

	@PostMapping
	@Operation(summary = "creates new employee", responses = {
			@ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "401", description = "Invalid Bearer Token!"),
			@ApiResponse(responseCode = "403", description = "Don't have permission to access this resource")

	})
	public EmployeeDto insert(@Valid @RequestBody EmployeeDto emp) {

		return empDtoMapper.toDto(employeeService.insert(empDtoMapper.toEntity(emp)));
	}

	@PutMapping
	@Operation(summary = "updates existing employee ", responses = {
			@ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "404", description = "Employee not found , depending on empId"),
			@ApiResponse(responseCode = "401", description = "Invalid Bearer Token!"),
			@ApiResponse(responseCode = "403", description = "Don't have permission to access this resource")
			
	})
	public EmployeeDto update(@Valid @RequestBody EmployeeDto emp) {
		return empDtoMapper.toDto(employeeService.update(empDtoMapper.toEntity(emp)));
	}

	
	@DeleteMapping("/{empId}")
	@Operation(summary = "deletes existing employee ", responses = {
			@ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "401", description = "Invalid Bearer Token!"),
			@ApiResponse(responseCode = "403", description = "Don't have permission to access this resource")
			
	})
	public void deleteById(@PathVariable Long empId) {
		employeeService.deleteById(empId);
	}

}
