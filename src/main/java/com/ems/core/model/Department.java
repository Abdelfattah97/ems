package com.ems.core.model;

import com.ems.core.validation.annotation.MultiWord;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Department {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "dept_id")
	private Long deptId;
	
	@Column(name = "dept_name",unique = true)
	@MultiWord(message="Department name should Only contain one or more alphabetic words!")
	private String deptName;
	
}
