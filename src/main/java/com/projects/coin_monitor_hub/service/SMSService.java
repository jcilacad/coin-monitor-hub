package com.projects.coin_monitor_hub.service;

public interface SMSService {

    Object sendSms(String phoneNumber, String message);
}
