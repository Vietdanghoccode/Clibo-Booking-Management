package com.clibo.external.BankSystem;

import com.clibo.external.IBankSystem;
import com.clibo.external.ISMSSystem;
import org.springframework.stereotype.Component;

@Component
public class BankSystem implements IBankSystem {
    @Override
    public boolean sendOTP(String phoneNumber, String otp) {
        return true;
    }

    @Override
    public boolean verifyOTP(String otp) {
        return true; // test
    }


}
