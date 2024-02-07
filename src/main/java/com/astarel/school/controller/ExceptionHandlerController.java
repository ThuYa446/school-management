package com.astarel.school.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.astarel.school.exception.ApiErrorResponse;
import com.astarel.school.exception.ErrorHandler;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolation;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorHandler> handleException(MethodArgumentNotValidException ex) {

        ErrorHandler dto = new ErrorHandler(HttpStatus.BAD_REQUEST, "Validation error");

        dto.setDetailedMessage(ex.getBindingResult().getAllErrors().stream()
            .map(err -> err.unwrap(ConstraintViolation.class))
            .map(err -> String.format("'%s' %s", err.getPropertyPath(), err.getMessage()))
            .collect(Collectors.toList()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }
    
    @ExceptionHandler(ApiErrorResponse.class)
    public ResponseEntity<ApiErrorResponse> handleJwtExpiredException(ApiErrorResponse ex) {

    	ApiErrorResponse dto = new ApiErrorResponse(ex.getErrorCode(), ex.getMessage());


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }
}