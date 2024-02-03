package com.astarel.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	@PostMapping
	ResponseEntity<Object> saveTeacher(@RequestBody @Valid TeacherDto teacherDto,
									BindingResult result){
		log.info("POST saveTeacher "+teacherDto);
		if(result.hasErrors()){
			log.info("Validation error in creating Teacher "+result);
			return ResponseEntity.badRequest()
							 	.body(result.getAllErrors());	
		}else {
			TeacherDto savedTeacher = this.teacherService.saveTeacher(teacherDto);
			return ResponseEntity.status(HttpStatus.CREATED)
								.body(savedTeacher);
		}
	}

}
