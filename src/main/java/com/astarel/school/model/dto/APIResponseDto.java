package com.astarel.school.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class APIResponseDto {
	private String code;
	
	private String message;
}
