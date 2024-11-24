package com.projects.coin_monitor_hub.service.impl;

import com.projects.coin_monitor_hub.service.SMSService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SMSServiceImpl implements SMSService {

    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;

    @Override
    public Object sendSms(String toPhoneNumber, String message) {
        log.debug("sendSms({}, {})", toPhoneNumber, message);
        PhoneNumber to = new PhoneNumber(toPhoneNumber);
        PhoneNumber from = new PhoneNumber(twilioPhoneNumber);
        return Message.creator(to, from, message).create();
    }
}
