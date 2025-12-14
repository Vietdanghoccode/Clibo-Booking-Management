package com.clibo.application;

import com.clibo.domain.profile.Patient;
import com.clibo.dto.RegisterRequest;
import com.clibo.external.ISMSSystem;
import com.clibo.persistence.ClinicDBManager;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthControl {
    private final ClinicDBManager dbManager;
    private final ISMSSystem smsSystem;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String registerAccount(@RequestBody RegisterRequest request) {
        Patient patient = new Patient();
        patient.setFullName(request.getFullName());
        patient.setPhone(request.getPhoneNumber());
        patient.setPassword(passwordEncoder.encode(request.getPassword()));
        patient.setVerified(false);

        dbManager.save(patient);

        String otp = "123456"; // demo
        boolean sent = smsSystem.sendOTP(patient.getPhone(), otp);

        if (!sent) {
            throw new RuntimeException("Failed to send OTP");
        }

        return "OTP sent successfully";
    }
}
