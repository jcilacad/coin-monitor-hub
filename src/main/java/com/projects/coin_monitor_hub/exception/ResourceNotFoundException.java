package com.projects.coin_monitor_hub.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom exception for handling cases where a resource is not found.
 */
@Slf4j
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified resource name, field name, and field value.
     *
     * @param resourceName the name of the resource
     * @param fieldName the name of the field
     * @param fieldValue the value of the field
     */
    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
        log.error(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
    }
}
