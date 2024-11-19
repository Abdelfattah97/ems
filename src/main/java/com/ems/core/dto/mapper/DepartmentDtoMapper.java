package com.ems.core.dto.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.ems.core.dto.DepartmentDto;
import com.ems.core.model.Department;

@Mapper(componentModel = "spring")
@Component
public interface DepartmentDtoMapper {

	Department toEntity(DepartmentDto dto);
	
	DepartmentDto toDto(Department emp);
	
}
