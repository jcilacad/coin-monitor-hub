package com.projects.coin_monitor_hub.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom exception for handling email sending errors.
 */
@Slf4j
public class EmailSendingException extends RuntimeException {

    /**
     * Constructs a new EmailSendingException with the specified detail message.
     *
     * @param message the detail message
     */
    public EmailSendingException(String message) {
        super(message);
        log.error(message);
    }
}
