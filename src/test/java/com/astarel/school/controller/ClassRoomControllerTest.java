package com.astarel.school.controller;

import static com.astarel.school.TestDataFactory.classRoomDto;
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
import org.springframework.validation.BindingResult;

import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.model.dto.APIResponseDto;
import com.astarel.school.model.dto.ClassRoomDto;
import com.astarel.school.service.ClassRoomService;

@ExtendWith(MockitoExtension.class)
class ClassRoomControllerTest {

	@Mock
	private ClassRoomService classRoomService;

	private ClassRoomController controller;

	@BeforeEach
	void setUp() {
		controller = new ClassRoomController();
		controller.classRoomService = classRoomService;
	}

	@Test
	void getEndpointsDelegateToService() {
		when(classRoomService.getAllClassRoom()).thenReturn(List.of(classRoomDto(1L)));
		when(classRoomService.getClassRoomById(1L)).thenReturn(classRoomDto(1L));

		assertThat(controller.getAllClassRooms()).hasSize(1);
		assertThat(controller.getClassRoomById(1L).getClassName()).isEqualTo("Room 1");
	}

	@Test
	void saveClassRoomReturnsValidationErrorsWhenBindingFails() {
		ClassRoomDto dto = classRoomDto(1L);
		BindingResult result = new BeanPropertyBindingResult(dto, "classRoom");
		result.rejectValue("className", "required", "required");

		ResponseEntity<Object> response = controller.saveClassRoom(dto, result);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat((List<?>) response.getBody()).hasSize(1);
	}

	@Test
	void saveClassRoomReturnsCreatedRoomWhenServiceSucceeds() throws Exception {
		ClassRoomDto dto = classRoomDto(1L);
		when(classRoomService.saveClassRoom(dto)).thenReturn(dto);

		ResponseEntity<Object> response = controller.saveClassRoom(dto, new BeanPropertyBindingResult(dto, "classRoom"));

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isEqualTo(dto);
	}

	@Test
	void saveClassRoomReturnsServiceErrorPayload() throws Exception {
		ClassRoomDto dto = classRoomDto(1L);
		when(classRoomService.saveClassRoom(dto)).thenThrow(new ApiErrorResponse("1001", "duplicate"));

		ResponseEntity<Object> response = controller.saveClassRoom(dto, new BeanPropertyBindingResult(dto, "classRoom"));

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(((ApiErrorResponse) response.getBody()).getErrorCode()).isEqualTo("1001");
	}

	@Test
	void updateClassRoomReturnsSuccessAndMissingRoomBranches() throws Exception {
		ClassRoomDto dto = classRoomDto(1L);
		when(classRoomService.findClassRoomById(1L)).thenReturn(true, false);
		when(classRoomService.updateClassRoom(dto)).thenReturn(dto);

		ResponseEntity<Object> success = controller.updateClassRoom(1L, dto, new BeanPropertyBindingResult(dto, "classRoom"));
		ResponseEntity<Object> missing = controller.updateClassRoom(1L, dto, new BeanPropertyBindingResult(dto, "classRoom"));

		assertThat(success.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(success.getBody()).isEqualTo(dto);
		assertThat(missing.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(((ApiErrorResponse) missing.getBody()).getErrorCode()).isEqualTo("1002");
	}

	@Test
	void deleteClassRoomReturnsDeletedAndMissingRoomBranches() {
		doNothing().when(classRoomService).deleteClassRoomById(1L);
		when(classRoomService.findClassRoomById(1L)).thenReturn(true, false);

		ResponseEntity<Object> success = controller.deleteClassRoom(1L);
		ResponseEntity<Object> missing = controller.deleteClassRoom(1L);

		assertThat(success.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(success.getBody()).isEqualTo(new APIResponseDto("200", "Deleted Successfully"));
		assertThat(missing.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(((APIResponseDto) missing.getBody()).getCode()).isEqualTo("1002");
	}
}
