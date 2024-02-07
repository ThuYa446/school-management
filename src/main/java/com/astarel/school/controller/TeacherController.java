package com.astarel.school.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.model.dto.APIResponseDto;
import com.astarel.school.model.dto.TeacherDto;
import com.astarel.school.service.TeacherService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
	
	@Autowired
	TeacherService teacherService;
	
	@GetMapping
	List<TeacherDto> getAllTeachers(){
		return this.teacherService.getAllTeachers();
	}
	
	@PostMapping
	ResponseEntity<Object> saveTeacher(@RequestBody @Valid TeacherDto teacher,
									BindingResult result){
		try {
			log.info("POST saveTeacher "+teacher);
			if(result.hasErrors()){
				log.info("Validation error in creating teacher "+result);
				return ResponseEntity.badRequest()
								 	.body(result.getAllErrors());	
			}else {
				TeacherDto savedTeacher = this.teacherService.saveTeacher(teacher);
				return ResponseEntity.status(HttpStatus.CREATED)
									.body(savedTeacher);
			}
		}catch(ApiErrorResponse error) {
			return ResponseEntity.badRequest()
					.body(new ApiErrorResponse(error.getErrorCode(), error.getMessage()));
		}
	}
	
	@PutMapping("/{teacherId}")
	ResponseEntity<Object> updateTeacher(@PathVariable Long teacherId, @RequestBody @Valid TeacherDto teacher,
									BindingResult result){
		try {
			log.info("PUT update teacher "+teacher);
			if(result.hasErrors()){
				log.info("Validation error in updating teacher "+result);
				return ResponseEntity.badRequest()
								 	.body(result.getAllErrors());	
			}else {
				if(this.teacherService.findTeacherById(teacherId)) {
					TeacherDto updatedTeacher = this.teacherService.updateTeacher(teacher);
					return ResponseEntity.status(HttpStatus.OK)
										.body(updatedTeacher);
				}else {
					return ResponseEntity.badRequest()
							.body(new ApiErrorResponse("1002", "No such teacher with id - "+teacherId+" found"));
				}
			}
		}catch(ApiErrorResponse error) {
			return ResponseEntity.badRequest()
					.body(new ApiErrorResponse(error.getErrorCode(), error.getMessage()));
		}
	}
	
	@DeleteMapping("/{teacherId}")
	ResponseEntity<Object> deleteTeacher(@PathVariable Long teacherId){
		log.info("DELETE delete Teacher "+ teacherId);
		
		if(this.teacherService.findTeacherById(teacherId)) {
			this.teacherService.deleteTeacherById(teacherId);
			return ResponseEntity.status(HttpStatus.OK)
									.body(new APIResponseDto("200","Deleted Successfully"));
		}else {
			return ResponseEntity.badRequest()
						.body(new APIResponseDto("1002", "No such teacher with id - "+teacherId+" found"));
		}
	}
}
