package com.astarel.school.model.dto;

import java.util.Date;
import java.util.List;

import com.astarel.school.model.entity.Student;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClassRoomDto {
	
	private Long id;
	
	@NotBlank(message = "{required.classroom.name}")
	@NotNull(message = "{required.classroom.name}")
	private String className;
	
	private List<StudentDto> studentDto;
	
	Date createdAt;

	Date updatedAt;
}
