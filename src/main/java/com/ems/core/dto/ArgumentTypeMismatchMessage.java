package com.ems.core.dto;

import lombok.Data;

@Data
public class ArgumentTypeMismatchMessage {
	private String message = "request doesn't follow API specifications";
	private String field;
	private String value;
	private String ref="/doc";
}
