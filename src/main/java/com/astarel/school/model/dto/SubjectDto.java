package com.astarel.school.model.dto;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubjectDto {
	
	private Long id;
	
	@NotBlank(message = "{required.subject.title}")
	@NotNull(message = "{required.subject.title}")
	private String title;
	
	private List<StudentDto> studentDto;
	
	private Date createdAt;

	private Date updatedAt;

}
