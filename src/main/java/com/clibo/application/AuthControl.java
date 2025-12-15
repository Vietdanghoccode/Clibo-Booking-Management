package com.clibo.application;

import com.clibo.domain.profile.Patient;
import com.clibo.domain.profile.User;
import com.clibo.dto.LoginRequest;
import com.clibo.dto.RegisterRequest;
import com.clibo.external.ISMSSystem;
import com.clibo.persistence.ClinicDBManager;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        // Check account existed
        if (dbManager.getUserInformation(request.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("Account already exists");
        }

        String otp = "123456";
        boolean sent = smsSystem.sendOTP(request.getPhoneNumber(), otp);

        if (!sent) {
            throw new RuntimeException("OTP failed");
        }

        Patient patient = new Patient();
        patient.setFullName(request.getFullName());
        patient.setPhone(request.getPhoneNumber());
        patient.setPassword(passwordEncoder.encode(request.getPassword()));
        patient.setVerified(true);

        dbManager.createPatient(patient);

        return "OTP sent successfully";
    }


    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest request) {

        // 1. getUser(phoneNumber)
        User user = dbManager.getUserInformation(request.getPhoneNumber())
                .orElseThrow(() ->
                        new RuntimeException("Account not found")
                );

        // 2. validateUserPassword(inputPassword)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid password");
        }

        // 3. setupSecurityContext()
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        // 4. return success
        return ResponseEntity.ok("Login successful");
    }
}
