package com.clibo.external;

public interface ISMSSystem {
    boolean sendOTP(String phoneNumber, String otp);
    boolean verifyOTP(String otp);
}
