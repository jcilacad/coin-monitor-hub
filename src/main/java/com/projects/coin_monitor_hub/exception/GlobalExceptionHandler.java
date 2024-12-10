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

    @ExceptionHandler(CoinAlreadyExistsException.class)
    public ResponseEntity<ErrorDetailsResponse> handleCoinAlreadyExistsException(CoinAlreadyExistsException coinAlreadyExistsException,
                                                                                 WebRequest webRequest) {
        ErrorDetailsResponse errorDetailsResponse = new ErrorDetailsResponse();
        errorDetailsResponse.setTimestamp(LocalDateTime.now());
        errorDetailsResponse.setMessage(coinAlreadyExistsException.getMessage());
        errorDetailsResponse.setPath(webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<ErrorDetailsResponse> handleEmailSendingException(EmailSendingException emailSendingException,
                                                                            WebRequest webRequest) {
        ErrorDetailsResponse errorDetailsResponse = new ErrorDetailsResponse();
        errorDetailsResponse.setTimestamp(LocalDateTime.now());
        errorDetailsResponse.setMessage(emailSendingException.getMessage());
        errorDetailsResponse.setPath(webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ApiResponseParsingException.class)
    public ResponseEntity<ErrorDetailsResponse> handleApiResponseParsingException(ApiResponseParsingException apiResponseParsingException,
                                                                                  WebRequest webRequest) {
        ErrorDetailsResponse errorDetailsResponse = new ErrorDetailsResponse();
        errorDetailsResponse.setTimestamp(LocalDateTime.now());
        errorDetailsResponse.setMessage(apiResponseParsingException.getMessage());
        errorDetailsResponse.setPath(webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsResponse, HttpStatus.BAD_GATEWAY);
    }

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
