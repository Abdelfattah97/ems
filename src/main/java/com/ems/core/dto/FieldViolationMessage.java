package com.ems.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FieldViolationMessage {

	String field;
	
	String message;

	@Override
	public String toString() {
		return String.format("{field=%s , message=%s}", field ,message);
	}
	
}
