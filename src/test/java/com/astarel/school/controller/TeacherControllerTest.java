package com.astarel.school.controller;

import static com.astarel.school.TestDataFactory.teacherDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;

import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.model.dto.APIResponseDto;
import com.astarel.school.model.dto.TeacherDto;
import com.astarel.school.service.TeacherService;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

	@Mock
	private TeacherService teacherService;

	private TeacherController controller;

	@BeforeEach
	void setUp() {
		controller = new TeacherController();
		controller.teacherService = teacherService;
	}

	@Test
	void getEndpointsDelegateToService() {
		when(teacherService.getAllTeachers()).thenReturn(List.of(teacherDto(1L)));
		when(teacherService.getTeacherById(1L)).thenReturn(teacherDto(1L));

		assertThat(controller.getAllTeachers()).hasSize(1);
		assertThat(controller.getTeacherById(1L).getEmail()).isEqualTo("teacher1@school.com");
	}

	@Test
	void saveTeacherCoversValidationSuccessAndServiceError() throws Exception {
		TeacherDto dto = teacherDto(1L);
		BeanPropertyBindingResult invalid = new BeanPropertyBindingResult(dto, "teacher");
		invalid.rejectValue("name", "required", "required");

		ResponseEntity<Object> validation = controller.saveTeacher(dto, invalid);

		when(teacherService.saveTeacher(dto)).thenReturn(dto);
		ResponseEntity<Object> success = controller.saveTeacher(dto, new BeanPropertyBindingResult(dto, "teacher"));

		when(teacherService.saveTeacher(dto)).thenThrow(new ApiErrorResponse("1001", "duplicate"));
		ResponseEntity<Object> error = controller.saveTeacher(dto, new BeanPropertyBindingResult(dto, "teacher"));

		assertThat(validation.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(success.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(((ApiErrorResponse) error.getBody()).getErrorCode()).isEqualTo("1001");
	}

	@Test
	void updateTeacherReturnsSuccessAndMissingTeacherBranches() throws Exception {
		TeacherDto dto = teacherDto(1L);
		when(teacherService.findTeacherById(1L)).thenReturn(true, false);
		when(teacherService.updateTeacher(dto)).thenReturn(dto);

		ResponseEntity<Object> success = controller.updateTeacher(1L, dto, new BeanPropertyBindingResult(dto, "teacher"));
		ResponseEntity<Object> missing = controller.updateTeacher(1L, dto, new BeanPropertyBindingResult(dto, "teacher"));

		assertThat(success.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(((ApiErrorResponse) missing.getBody()).getErrorCode()).isEqualTo("1002");
	}

	@Test
	void deleteTeacherReturnsDeletedAndMissingTeacherBranches() {
		when(teacherService.findTeacherById(1L)).thenReturn(true, false);

		ResponseEntity<Object> success = controller.deleteTeacher(1L);
		ResponseEntity<Object> missing = controller.deleteTeacher(1L);

		assertThat(success.getBody()).isEqualTo(new APIResponseDto("200", "Deleted Successfully"));
		assertThat(((APIResponseDto) missing.getBody()).getCode()).isEqualTo("1002");
	}
}
