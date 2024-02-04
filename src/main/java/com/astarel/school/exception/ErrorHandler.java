package com.astarel.school.exception;

import java.util.List;
import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ErrorHandler {
    private final int status;
    private final String error;
    private final String message;
    private List<String> detailedMessage;

    public ErrorHandler(HttpStatus httpStatus, String message) {
        status = httpStatus.value();
        error = httpStatus.getReasonPhrase();
        this.message = message;
    }
   
}
