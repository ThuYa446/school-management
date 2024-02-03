package com.astarel.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astarel.school.model.dto.SubjectDto;
import com.astarel.school.service.SubjectService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

	@Autowired
	SubjectService subjectService;
	
	@PostMapping
	ResponseEntity<Object> saveSubject(@RequestBody @Valid SubjectDto subjectDto,
									BindingResult result){
		log.info("POST saveSubject "+subjectDto);
		if(result.hasErrors()){
			log.info("Validation error in creating Subject "+result);
			return ResponseEntity.badRequest()
							 	.body(result.getAllErrors());	
		}else {
			SubjectDto savedSubject = this.subjectService.saveSubject(subjectDto);
			return ResponseEntity.status(HttpStatus.CREATED)
								.body(savedSubject);
		}
	}
	
}
