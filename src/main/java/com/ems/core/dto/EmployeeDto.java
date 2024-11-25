package com.ems.core.dto;

import com.ems.core.validation.annotation.MultiWord;
import com.ems.core.validation.annotation.SingleWord;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeDto {

	private Long empId;

	@NotBlank(message = "Employee's first name is required!")
	@SingleWord
	private String firstName;

	@NotBlank(message = "Employee's last name is required!")
	@SingleWord
	private String lastName;

	@Email(message = "Provided Email is not valid!")
	@NotBlank(message = "Employee's email is required!")
	private String email;

	@NotBlank(message = "Employee's position is required!")
	@MultiWord(message = "Position should only contain one or more alphabetic words!")
	private String position;

	@NotNull(message = "Employee's salary is required!")
	@Min(value = 1, message = "the min allowed value for employee's salary is 1")
	@Max(value = 100000, message = "the max allowed value for employee's salary is 100000")
	private Double salary;

	@NotNull(message = "The Employee's Department Id is mandatory!")
	private Long deptId;

	private String deptName;

}
