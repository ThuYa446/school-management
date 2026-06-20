package com.astarel.school.controller;

import static com.astarel.school.TestDataFactory.studentDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
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
import com.astarel.school.model.dto.StudentDto;
import com.astarel.school.service.StudentService;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

	@Mock
	private StudentService studentService;

	private StudentController controller;

	@BeforeEach
	void setUp() {
		controller = new StudentController();
		controller.studentService = studentService;
	}

	@Test
	void getEndpointsDelegateToService() {
		when(studentService.getAllStudents()).thenReturn(List.of(studentDto(1L)));
		when(studentService.getStudentNotEnrolledSubject()).thenReturn(List.of(studentDto(2L)));
		when(studentService.getStudentNotJoinedClass()).thenReturn(List.of(studentDto(3L)));
		when(studentService.getStudentById(1L)).thenReturn(studentDto(1L));

		assertThat(controller.getAllStudents()).hasSize(1);
		assertThat(controller.getNotEnrolledStudents()).hasSize(1);
		assertThat(controller.getNotJoinedStudents()).hasSize(1);
		assertThat(controller.getStudentById(1L).getEmail()).isEqualTo("student1@school.com");
	}

	@Test
	void saveStudentCoversValidationSuccessAndServiceError() throws Exception {
		StudentDto dto = studentDto(1L);
		BeanPropertyBindingResult invalid = new BeanPropertyBindingResult(dto, "student");
		invalid.rejectValue("name", "required", "required");

		ResponseEntity<Object> validation = controller.saveStudent(dto, invalid);

		when(studentService.saveStudent(dto)).thenReturn(dto);
		ResponseEntity<Object> success = controller.saveStudent(dto, new BeanPropertyBindingResult(dto, "student"));

		when(studentService.saveStudent(dto)).thenThrow(new ApiErrorResponse("1001", "duplicate"));
		ResponseEntity<Object> error = controller.saveStudent(dto, new BeanPropertyBindingResult(dto, "student"));

		assertThat(validation.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(success.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(error.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(((ApiErrorResponse) error.getBody()).getErrorCode()).isEqualTo("1001");
	}

	@Test
	void updateStudentReturnsSuccessAndMissingStudentBranches() throws Exception {
		StudentDto dto = studentDto(1L);
		when(studentService.findStudetById(1L)).thenReturn(true, false);
		when(studentService.updateStudent(dto)).thenReturn(dto);

		ResponseEntity<Object> success = controller.updateStudent(1L, dto, new BeanPropertyBindingResult(dto, "student"));
		ResponseEntity<Object> missing = controller.updateStudent(1L, dto, new BeanPropertyBindingResult(dto, "student"));

		assertThat(success.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(missing.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(((ApiErrorResponse) missing.getBody()).getErrorCode()).isEqualTo("1002");
	}

	@Test
	void deleteStudentReturnsDeletedAndMissingStudentBranches() {
		doNothing().when(studentService).deleteStudentById(1L);
		when(studentService.findStudetById(1L)).thenReturn(true, false);

		ResponseEntity<Object> success = controller.deleteStudent(1L);
		ResponseEntity<Object> missing = controller.deleteStudent(1L);

		assertThat(success.getBody()).isEqualTo(new APIResponseDto("200", "Deleted Successfully"));
		assertThat(((APIResponseDto) missing.getBody()).getCode()).isEqualTo("1002");
	}
}
