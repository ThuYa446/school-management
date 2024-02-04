package com.astarel.school.model.dto;

import java.util.Date;

import com.astarel.school.model.entity.StudentType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class StudentDto {

	private Long id;
	
	@NotBlank(message = "{required.user.name}")
	@NotNull(message = "{required.user.name}")
	private String name;
	
	@NotBlank(message = "{required.user.email}")
	@NotNull(message = "{required.user.email}")
	private String email;
	
	@NotBlank(message = "{required.user.phoneno}")
	@NotNull(message = "{required.user.phoneno}")
	@Size(min = 7, max = 15, message = "{size.user.phoneno}")
	private String phoneNo;
	
	@NotBlank(message = "{required.user.address}")
	@NotNull(message = "{required.user.address}")
	private String address;
	
	@NotNull(message = "{required.student.type}")
	private StudentType studentType;
	
	private ClassRoomDto classRoomDto;
	
	Date createdAt;

	Date updatedAt;
}
