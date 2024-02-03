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
import com.astarel.school.model.dto.StudentDto;
import com.astarel.school.service.StudentService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/students")
public class StudentController {
	
	@Autowired
	StudentService studentService;
	
	@GetMapping
	List<StudentDto> getAllStudents(){
		return this.studentService.getAllStudents();
	}

	@PostMapping
	ResponseEntity<Object> saveStudent(@RequestBody @Valid StudentDto student,
									BindingResult result) throws ApiErrorResponse{
		log.info("POST saveStudent "+student);
		if(result.hasErrors()){
			log.info("Validation error in creating student "+result);
			return ResponseEntity.badRequest()
							 	.body(result.getAllErrors());	
		}else {
			StudentDto savedStudent = this.studentService.saveStudent(student);
			return ResponseEntity.status(HttpStatus.CREATED)
								.body(savedStudent);
		}
	}
	
	@PutMapping("/{studentId}")
	ResponseEntity<Object> updateStudent(@PathVariable Long studentId, @RequestBody @Valid StudentDto student,
									BindingResult result) throws ApiErrorResponse{
		log.info("PUT update Student "+student);
		if(result.hasErrors()){
			log.info("Validation error in updating Student "+result);
			return ResponseEntity.badRequest()
							 	.body(result.getAllErrors());	
		}else {
			if(this.studentService.findStudetById(studentId)) {
				StudentDto updatedStudent = this.studentService.updateStudent(student);
				return ResponseEntity.status(HttpStatus.OK)
									.body(updatedStudent);
			}else {
				return ResponseEntity.badRequest()
						.body(new ApiErrorResponse("1002", "No such student with id - "+studentId+" found"));
			}
		}
	}
	
	@DeleteMapping("/{studentId}")
	ResponseEntity<Object> deleteStudent(@PathVariable Long studentId){
		log.info("DELETE delete Student "+ studentId);
		
		if(this.studentService.findStudetById(studentId)) {
			this.studentService.deleteStudentById(studentId);
			return ResponseEntity.status(HttpStatus.OK)
									.body(new APIResponseDto("200","Deleted Successfully"));
		}else {
			return ResponseEntity.badRequest()
						.body(new APIResponseDto("1002", "No such student with id - "+studentId+" found"));
		}
	}
}
