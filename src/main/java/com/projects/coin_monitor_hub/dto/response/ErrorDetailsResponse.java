package com.projects.coin_monitor_hub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetailsResponse {
    private LocalDateTime timestamp;
    private String message;
    private String path;
}
