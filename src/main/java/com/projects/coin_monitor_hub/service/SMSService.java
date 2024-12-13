package com.projects.coin_monitor_hub.service;

/**
 * Service interface for SMS sending operations.
 */
public interface SMSService {

    /**
     * Sends an SMS message.
     *
     * @param phoneNumber the recipient's phone number
     * @param message the content of the SMS message
     * @return the response from the SMS service
     */
    Object sendSms(String phoneNumber, String message);
}
