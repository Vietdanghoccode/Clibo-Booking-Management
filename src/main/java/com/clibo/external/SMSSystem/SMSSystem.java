package com.clibo.external.SMSSystem;

import org.springframework.stereotype.Component;

import com.clibo.external.ISMSSystem;

@Component
public class SMSSystem implements ISMSSystem {
    @Override
    public boolean sendOTP(String phoneNumber, String otp) {
        return true; // test
    }

    @Override
    public boolean verifyOTP(String otp) {
        return true; // test
    }


}
