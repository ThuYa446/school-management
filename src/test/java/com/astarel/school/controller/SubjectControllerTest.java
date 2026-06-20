package com.astarel.school.controller;

import static com.astarel.school.TestDataFactory.subjectDto;
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
import com.astarel.school.model.dto.SubjectDto;
import com.astarel.school.service.SubjectService;

@ExtendWith(MockitoExtension.class)
class SubjectControllerTest {

	@Mock
	private SubjectService subjectService;

	private SubjectController controller;

	@BeforeEach
	void setUp() {
		controller = new SubjectController();
		controller.subjectService = subjectService;
	}

	@Test
	void getEndpointsDelegateToService() {
		when(subjectService.getAllSubjects()).thenReturn(List.of(subjectDto(1L)));
		when(subjectService.getSubjectById(1L)).thenReturn(subjectDto(1L));
		when(subjectService.getSubjectNotTeachByTeacher()).thenReturn(List.of(subjectDto(2L)));

		assertThat(controller.getAllSubjects()).hasSize(1);
		assertThat(controller.getSubjectById(1L).getTitle()).isEqualTo("Subject 1");
		assertThat(controller.getAllSubjectNotToughByTeacher()).hasSize(1);
	}

	@Test
	void saveSubjectCoversValidationSuccessAndServiceError() throws Exception {
		SubjectDto dto = subjectDto(1L);
		BeanPropertyBindingResult invalid = new BeanPropertyBindingResult(dto, "subject");
		invalid.rejectValue("title", "required", "required");

		ResponseEntity<Object> validation = controller.saveSubject(dto, invalid);

		when(subjectService.saveSubject(dto)).thenReturn(dto);
		ResponseEntity<Object> success = controller.saveSubject(dto, new BeanPropertyBindingResult(dto, "subject"));

		when(subjectService.saveSubject(dto)).thenThrow(new ApiErrorResponse("1001", "duplicate"));
		ResponseEntity<Object> error = controller.saveSubject(dto, new BeanPropertyBindingResult(dto, "subject"));

		assertThat(validation.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(success.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(((ApiErrorResponse) error.getBody()).getErrorCode()).isEqualTo("1001");
	}

	@Test
	void updateSubjectReturnsSuccessAndMissingSubjectBranches() throws Exception {
		SubjectDto dto = subjectDto(1L);
		when(subjectService.findSubjectById(1L)).thenReturn(true, false);
		when(subjectService.updateSubject(dto)).thenReturn(dto);

		ResponseEntity<Object> success = controller.updateSubject(1L, dto, new BeanPropertyBindingResult(dto, "subject"));
		ResponseEntity<Object> missing = controller.updateSubject(1L, dto, new BeanPropertyBindingResult(dto, "subject"));

		assertThat(success.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(((ApiErrorResponse) missing.getBody()).getErrorCode()).isEqualTo("1002");
	}

	@Test
	void deleteSubjectReturnsDeletedAndMissingSubjectBranches() {
		doNothing().when(subjectService).deleteSubjectById(1L);
		when(subjectService.findSubjectById(1L)).thenReturn(true, false);

		ResponseEntity<Object> success = controller.deleteSubject(1L);
		ResponseEntity<Object> missing = controller.deleteSubject(1L);

		assertThat(success.getBody()).isEqualTo(new APIResponseDto("200", "Deleted Successfully"));
		assertThat(((APIResponseDto) missing.getBody()).getCode()).isEqualTo("1002");
	}
}
