package com.projects.coin_monitor_hub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for error details response.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetailsResponse {

    /**
     * The timestamp when the error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * The error message.
     */
    private String message;

    /**
     * The path where the error occurred.
     */
    private String path;
}
