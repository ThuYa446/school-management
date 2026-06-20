package com.astarel.school.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.exception.ErrorHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;

class ExceptionHandlerControllerTest {

	private final ExceptionHandlerController controller = new ExceptionHandlerController();

	@Test
	void handleExceptionBuildsValidationErrorPayload() {
		MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
		BindingResult bindingResult = mock(BindingResult.class);
		ObjectError error = mock(ObjectError.class);
		@SuppressWarnings("unchecked")
		ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
		Path propertyPath = mock(Path.class);

		when(exception.getBindingResult()).thenReturn(bindingResult);
		when(bindingResult.getAllErrors()).thenReturn(List.of(error));
		when(error.unwrap(ConstraintViolation.class)).thenReturn(violation);
		when(violation.getPropertyPath()).thenReturn(propertyPath);
		when(propertyPath.toString()).thenReturn("email");
		when(violation.getMessage()).thenReturn("must not be blank");

		ResponseEntity<ErrorHandler> response = controller.handleException(exception);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody().getDetailedMessage()).containsExactly("'email' must not be blank");
	}

	@Test
	void handleJwtExpiredExceptionReturnsApiErrorResponse() {
		ApiErrorResponse error = new ApiErrorResponse("401", "expired");

		ResponseEntity<ApiErrorResponse> response = controller.handleJwtExpiredException(error);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody().getErrorCode()).isEqualTo("401");
		assertThat(response.getBody().getMessage()).isEqualTo("expired");
	}
}
