package com.projects.coin_monitor_hub.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom exception for handling API response parsing errors.
 */
@Slf4j
public class ApiResponseParsingException extends RuntimeException {

    /**
     * Constructs a new ApiResponseParsingException with the specified detail message.
     *
     * @param message the detail message
     */
    public ApiResponseParsingException(String message) {
        super(message);
        log.error(message);
    }
}

