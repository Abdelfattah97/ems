package com.ems.core.dto;

import com.ems.core.validation.annotation.MultiWord;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DepartmentDto {

	@NotBlank(message = "Department name is mandatory")
	@MultiWord(message = "Department name should only contain one or more alphabetic words!")
	private String deptName;
	@NotNull(message = "department id is mandatory")
	private Long deptId;
	
}
