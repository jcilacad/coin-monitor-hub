package com.projects.coin_monitor_hub.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailSendingException extends RuntimeException {

    public EmailSendingException(String message) {
        super(message);
        log.error(message);
    }
}
