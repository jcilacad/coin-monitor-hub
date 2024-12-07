package com.projects.coin_monitor_hub.exception;

import com.projects.coin_monitor_hub.dto.response.ErrorDetailsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetailsResponse> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException,
                                                                                WebRequest webRequest) {
        ErrorDetailsResponse errorDetailsResponse = new ErrorDetailsResponse();
        errorDetailsResponse.setTimestamp(LocalDateTime.now());
        errorDetailsResponse.setMessage(resourceNotFoundException.getMessage());
        errorDetailsResponse.setPath(webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsResponse, HttpStatus.NOT_FOUND);
    }
}
