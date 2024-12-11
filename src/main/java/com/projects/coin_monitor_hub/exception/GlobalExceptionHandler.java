package com.projects.coin_monitor_hub.exception;

import com.projects.coin_monitor_hub.dto.response.ErrorDetailsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CoinAlreadyExistsException.class)
    public ResponseEntity<ErrorDetailsResponse> handleCoinAlreadyExistsException(CoinAlreadyExistsException coinAlreadyExistsException,
                                                                                 WebRequest webRequest) {
        log.debug("handleCoinAlreadyExistsException({}, {})", coinAlreadyExistsException.getMessage(), webRequest.getContextPath());
        ErrorDetailsResponse errorDetailsResponse = new ErrorDetailsResponse();
        errorDetailsResponse.setTimestamp(LocalDateTime.now());
        errorDetailsResponse.setMessage(coinAlreadyExistsException.getMessage());
        errorDetailsResponse.setPath(webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<ErrorDetailsResponse> handleEmailSendingException(EmailSendingException emailSendingException,
                                                                            WebRequest webRequest) {
        log.debug("handleEmailSendingException({}, {})", emailSendingException.getMessage(), webRequest.getContextPath());
        ErrorDetailsResponse errorDetailsResponse = new ErrorDetailsResponse();
        errorDetailsResponse.setTimestamp(LocalDateTime.now());
        errorDetailsResponse.setMessage(emailSendingException.getMessage());
        errorDetailsResponse.setPath(webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ApiResponseParsingException.class)
    public ResponseEntity<ErrorDetailsResponse> handleApiResponseParsingException(ApiResponseParsingException apiResponseParsingException,
                                                                                  WebRequest webRequest) {
        log.debug("handleApiResponseParsingException({}, {})", apiResponseParsingException.getMessage(), webRequest.getContextPath());
        ErrorDetailsResponse errorDetailsResponse = new ErrorDetailsResponse();
        errorDetailsResponse.setTimestamp(LocalDateTime.now());
        errorDetailsResponse.setMessage(apiResponseParsingException.getMessage());
        errorDetailsResponse.setPath(webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsResponse, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetailsResponse> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException,
                                                                                WebRequest webRequest) {
        log.debug("handleResourceNotFoundException({}, {})", resourceNotFoundException.getMessage(), webRequest.getContextPath());
        ErrorDetailsResponse errorDetailsResponse = new ErrorDetailsResponse();
        errorDetailsResponse.setTimestamp(LocalDateTime.now());
        errorDetailsResponse.setMessage(resourceNotFoundException.getMessage());
        errorDetailsResponse.setPath(webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        log.debug("handleMethodArgumentNotValidException({})", methodArgumentNotValidException.getMessage());
        Map<String, String> errors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    errors.put(fieldName, message);
                });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
