package com.astarel.school.service;

import java.util.List;

import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.model.dto.TeacherDto;

public interface TeacherService {
	
	List<TeacherDto> getAllTeachers();
	
	Boolean isExistTeacher(String email);
	
	Boolean findTeacherById(Long id);
	
	TeacherDto getTeacherById(Long id);
	
	TeacherDto saveTeacher(TeacherDto studentDto) throws ApiErrorResponse;
	
	TeacherDto updateTeacher(TeacherDto studetnDto) throws ApiErrorResponse;
	
	void deleteTeacherById(Long id);

}
