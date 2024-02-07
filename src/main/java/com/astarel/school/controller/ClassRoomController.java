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
import com.astarel.school.service.ClassRoomService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/class-rooms")
public class ClassRoomController {
	
	@Autowired
	ClassRoomService classRoomService;
	
	@GetMapping
	List<ClassRoomDto> getAllClassRooms(){
		return this.classRoomService.getAllClassRoom();
	}

	@PostMapping
	ResponseEntity<Object> saveClassRoom(@RequestBody @Valid ClassRoomDto classRoom,
									BindingResult result){
		try {
			log.info("POST saveClassRoom "+classRoom);
			if(result.hasErrors()){
				log.info("Validation error in creating ClassRoom "+result);
				return ResponseEntity.badRequest()
								 	.body(result.getAllErrors());	
			}else {
				ClassRoomDto savedClassRoom = this.classRoomService.saveClassRoom(classRoom);
				return ResponseEntity.status(HttpStatus.CREATED)
									.body(savedClassRoom);
			}
		}catch(ApiErrorResponse error) {
			return ResponseEntity.badRequest()
					.body(new ApiErrorResponse(error.getErrorCode(), error.getMessage()));
		}
	}
	
	@PutMapping("/{roomId}")
	ResponseEntity<Object> updateClassRoom(@PathVariable Long roomId, @RequestBody @Valid ClassRoomDto classRoom,
									BindingResult result){
		
		try {
			log.info("PUT updateClassRoom "+classRoom);
			if(result.hasErrors()){
				log.info("Validation error in updating ClassRoom "+result);
				return ResponseEntity.badRequest()
								 	.body(result.getAllErrors());	
			}else {
				if(this.classRoomService.findClassRoomById(roomId)) {
					ClassRoomDto updatedClassRoom = this.classRoomService.updateClassRoom(classRoom);
					return ResponseEntity.status(HttpStatus.OK)
										.body(updatedClassRoom);
				}else {
					return ResponseEntity.badRequest()
							.body(new ApiErrorResponse("1002", "No such class room with id - "+roomId+" found"));
				}
			}
		}catch(ApiErrorResponse error) {
			return ResponseEntity.badRequest()
					.body(new ApiErrorResponse(error.getErrorCode(), error.getMessage()));
		}
		
	}
	
	@DeleteMapping("/{roomId}")
	ResponseEntity<Object> deleteClassRoom(@PathVariable Long roomId){
		log.info("DELETE deleteClassRoom "+roomId);
		
		if(this.classRoomService.findClassRoomById(roomId)) {
			this.classRoomService.deleteClassRoomById(roomId);
			return ResponseEntity.status(HttpStatus.OK)
									.body(new APIResponseDto("200","Deleted Successfully"));
		}else {
			return ResponseEntity.badRequest()
						.body(new APIResponseDto("1002", "No such class room with id - "+roomId+" found"));
		}
	}
}
