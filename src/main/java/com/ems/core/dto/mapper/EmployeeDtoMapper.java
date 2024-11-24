package com.ems.core.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.ems.core.dto.EmployeeDto;
import com.ems.core.model.Employee;

@Mapper(componentModel = "spring" ,uses = DepartmentDtoMapper.class)
@Component
public interface EmployeeDtoMapper {

	@Mapping(target = "department.deptId", source ="deptId")
	@Mapping(target = "department.deptName", source ="deptName")
	Employee toEntity(EmployeeDto dto);
	
	@Mapping(source = "department.deptId", target ="deptId")
	@Mapping(source = "department.deptName", target ="deptName")
	EmployeeDto toDto(Employee emp);
	
}
