package com.astarel.school.service;

import java.util.List;

import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.model.dto.StudentDto;

public interface StudentService {

	List<StudentDto> getAllStudents();
	
	Boolean isExistStudent(String email);
	
	Boolean findStudetById(Long Id);
	
	StudentDto saveStudent(StudentDto studentDto) throws ApiErrorResponse;
	
	StudentDto updateStudent(StudentDto studetnDto) throws ApiErrorResponse;
	
	void deleteStudentById(Long id);
}
