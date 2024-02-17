package com.astarel.school.service;

import java.util.List;

import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.model.dto.ClassRoomDto;


public interface ClassRoomService {
	
	List<ClassRoomDto> getAllClassRoom();
	
	ClassRoomDto getClassRoomById(Long id);
	
	Boolean isExistClassRoom(String className);
	
	Boolean findClassRoomById(Long Id);
	
	ClassRoomDto saveClassRoom(ClassRoomDto classRoomDto) throws ApiErrorResponse;
	
	ClassRoomDto updateClassRoom(ClassRoomDto classRoomDto) throws ApiErrorResponse;
	
	void deleteClassRoomById(Long id);

}
