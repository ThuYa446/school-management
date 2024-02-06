package com.astarel.school.service;

import java.util.List;

import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.model.dto.SubjectDto;

public interface SubjectService {
	
	List<SubjectDto> getAllSubjects();
	
	Boolean isExistSubject(String title);
	
	Boolean findSubjectById(Long Id);
	
	SubjectDto saveSubject(SubjectDto subjectDto) throws ApiErrorResponse;
	
	SubjectDto updateSubject(SubjectDto subjectDto) throws ApiErrorResponse;
	
	void deleteSubjectById(Long id);

}
