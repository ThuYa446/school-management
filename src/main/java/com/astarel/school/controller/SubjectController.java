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
import com.astarel.school.model.dto.ClassRoomDto;
import com.astarel.school.model.dto.StudentDto;
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
	
	@GetMapping
	List<SubjectDto> getAllSubjects(){
		return this.subjectService.getAllSubjects();
	}

	@PostMapping
	ResponseEntity<Object> saveSubject(@RequestBody @Valid SubjectDto subject,
									BindingResult result){
		try {
			log.info("POST saveSubject "+subject);
			if(result.hasErrors()){
				log.info("Validation error in creating Subject "+result);
				return ResponseEntity.badRequest()
								 	.body(result.getAllErrors());	
			}else {
				SubjectDto savedSubject = this.subjectService.saveSubject(subject);
				return ResponseEntity.status(HttpStatus.CREATED)
									.body(savedSubject);
			}
		}catch(ApiErrorResponse error) {
			return ResponseEntity.badRequest()
					.body(new ApiErrorResponse(error.getErrorCode(), error.getMessage()));
		}
	}
	
	@PutMapping("/{subjectId}")
	ResponseEntity<Object> updateSubject(@PathVariable Long subjectId, @RequestBody @Valid SubjectDto subject,
									BindingResult result){
		try {
			log.info("PUT update Subject "+subject);
			if(result.hasErrors()){
				log.info("Validation error in updating Subject "+result);
				return ResponseEntity.badRequest()
								 	.body(result.getAllErrors());	
			}else {
				if(this.subjectService.findSubjectById(subjectId)) {
					SubjectDto updatedSubject = this.subjectService.updateSubject(subject);
					return ResponseEntity.status(HttpStatus.OK)
										.body(updatedSubject);
				}else {
					return ResponseEntity.badRequest()
							.body(new ApiErrorResponse("1002", "No such subject with id - "+subjectId+" found"));
				}
			}
		}catch(ApiErrorResponse error) {
			return ResponseEntity.badRequest()
					.body(new ApiErrorResponse(error.getErrorCode(), error.getMessage()));
		}
	}
	
	@DeleteMapping("/{subjectId}")
	ResponseEntity<Object> deleteSubject(@PathVariable Long subjectId){
		log.info("DELETE deleteSubject "+subjectId);
		
		if(this.subjectService.findSubjectById(subjectId)) {
			this.subjectService.deleteSubjectById(subjectId);
			return ResponseEntity.status(HttpStatus.OK)
									.body(new APIResponseDto("200","Deleted Successfully"));
		}else {
			return ResponseEntity.badRequest()
						.body(new APIResponseDto("1002", "No such class room with id - "+subjectId+" found"));
		}
	}
	
}
