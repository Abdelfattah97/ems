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

import com.ems.core.dto.DepartmentDto;
import com.ems.core.dto.PageResponse;
import com.ems.core.dto.mapper.DepartmentDtoMapper;
import com.ems.core.dto.mapper.PageResponseDtoMapper;
import com.ems.core.model.Department;
import com.ems.core.service.DepartmentService;
import com.ems.core.service.pagination.PageRequestFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/departments")
@Tag(name = "Departments")
public class DepartmentController {

	@Autowired
	DepartmentService departmentService;
	@Autowired
	DepartmentDtoMapper deptMapper;
	@Autowired
	PageResponseDtoMapper<Department,DepartmentDto> pageResponseMapper;
	
	@GetMapping("/paged")
	@Operation(summary = "fetches department page", responses = {
			@ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "401", description = "Invalid Bearer Token!"),
			@ApiResponse(responseCode = "403", description = "Don't have permission to access this resource")
	})
	public PageResponse<DepartmentDto> findAllPageable(@RequestParam(required = false) Integer pgNum, @RequestParam(required = false) Integer pgSize, Direction dir,
			@RequestParam(defaultValue = "deptId") String[] sortBy) {
		Pageable pageable = PageRequestFactory.createPageRequest(pgSize, pgNum, dir, sortBy);
		return pageResponseMapper.toDto(departmentService.findAll(pageable), e->deptMapper.toDto(e));
	}

	@GetMapping("/{deptId}")
	@Operation(summary = "fetches department by its id", responses = {
			@ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "401", description = "Invalid Bearer Token!"),
			@ApiResponse(responseCode = "404", description = "no department found"),
			@ApiResponse(responseCode = "403", description = "Don't have permission to access this resource")
	})
	public Department findById( @PathVariable Long deptId) {
		return departmentService.findById(deptId);
	}

	@PostMapping
	@Operation(summary = "create new department", responses = {
			@ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "401", description = "Invalid Bearer Token!"),
			@ApiResponse(responseCode = "403", description = "Don't have permission to access this resource")
	})
	public DepartmentDto insert(@Valid @RequestBody DepartmentDto dept) {
		return  deptMapper.toDto(departmentService.insert(deptMapper.toEntity(dept)));
	}

	@PutMapping
	@Operation(summary = "updates existing department by its id", responses = {
			@ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "401", description = "Invalid Bearer Token!"),
			@ApiResponse(responseCode = "404", description = "no department found"),
			@ApiResponse(responseCode = "403", description = "Don't have permission to access this resource")
	})
	public DepartmentDto update(@Valid @RequestBody DepartmentDto dept) {
		return  deptMapper.toDto(departmentService.update(deptMapper.toEntity(dept)));
	}

	@DeleteMapping("/{deptId}")
	@Operation(summary = "updates existing department by its id", responses = {
			@ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "401", description = "Invalid Bearer Token!"),
			@ApiResponse(responseCode = "403", description = "Don't have permission to access this resource")
	})
	public void deleteById(@PathVariable Long deptId) {
		departmentService.deleteById(deptId);
	}

}
