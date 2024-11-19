package com.ems.core.model;

import com.ems.core.validation.annotation.MultiWord;
import com.ems.core.validation.annotation.SingleWord;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long empId;

	@Column(nullable = false)
	@NotNull(message = "Employee's first name is required!")
	@SingleWord
	private String firstName;

	@Column(nullable = false)	
	@NotNull(message = "Employee's last name is required!")
	@SingleWord
	private String lastName;

	@Column(nullable = false, unique = true)
	@Email(message = "Provided Email is not valid!")
	@NotNull(message = "Employee's email is required!")
	private String email;

	@Column(nullable = false)
	@NotNull(message = "Employee's position is required!")
	@MultiWord(message = "Position should only contain one or more alphabetic words!")
	private String position;
	
	@Column(nullable = false)
	@NotNull(message = "Employee's salary is required!")
	@Min(value = 1,message = "the min allowed value for employee's salary is 1")
	@Max(value = 100000,message = "the max allowed value for employee's salary is 100000")
	private Double salary;

	@ManyToOne()
	@JoinColumn(name = "dept_id",nullable = false)
	private Department department;

	public void setFirstName(String firstName) {
		this.firstName = firstName.trim();
	}
	
}
