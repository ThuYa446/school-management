package com.astarel.school.model.dto;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubjectDto {
	
	private Long id;
	
	@NotBlank(message = "{required.subject.code}")
	@NotNull(message = "{required.subject.code}")
	private String code;
	
	@NotBlank(message = "{required.subject.name}")
	@NotNull(message = "{required.subject.name}")
	private String name;
	
	private Date createdAt;

	private Date updatedAt;

}
