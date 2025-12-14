package com.clibo.external.SMSSystem;

import com.clibo.external.ISMSSystem;
import org.springframework.stereotype.Component;

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
