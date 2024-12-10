package com.projects.coin_monitor_hub.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiResponseParsingException extends RuntimeException {

    public ApiResponseParsingException(String message) {
        super(message);
        log.error(message);
    }
}
