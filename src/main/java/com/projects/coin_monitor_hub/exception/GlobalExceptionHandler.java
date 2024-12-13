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

/**
 * Global exception handler for handling various exceptions thrown by the application.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles CoinAlreadyExistsException.
     *
     * @param coinAlreadyExistsException the exception thrown when a coin already exists
     * @param webRequest                 the current web request
     * @return a ResponseEntity containing error details and HTTP status BAD_REQUEST
     */
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

    /**
     * Handles EmailSendingException.
     *
     * @param emailSendingException the exception thrown when an email fails to send
     * @param webRequest            the current web request
     * @return a ResponseEntity containing error details and HTTP status SERVICE_UNAVAILABLE
     */
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

    /**
     * Handles ApiResponseParsingException.
     *
     * @param apiResponseParsingException the exception thrown when an API response fails to parse
     * @param webRequest                  the current web request
     * @return a ResponseEntity containing error details and HTTP status BAD_GATEWAY
     */
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

    /**
     * Handles ResourceNotFoundException.
     *
     * @param resourceNotFoundException the exception thrown when a resource is not found
     * @param webRequest                the current web request
     * @return a ResponseEntity containing error details and HTTP status NOT_FOUND
     */
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

    /**
     * Handles MethodArgumentNotValidException.
     *
     * @param methodArgumentNotValidException the exception thrown when method arguments are not valid
     * @return a ResponseEntity containing validation errors and HTTP status BAD_REQUEST
     */
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
