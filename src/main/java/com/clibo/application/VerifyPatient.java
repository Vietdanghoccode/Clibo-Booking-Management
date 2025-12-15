package com.clibo.application;

import com.clibo.domain.profile.Patient;
import com.clibo.domain.profile.User;
import com.clibo.dto.CheckInRequest;
import com.clibo.dto.VerifyBody;
import com.clibo.persistence.ClinicDBManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verify-patient")
@AllArgsConstructor
public class VerifyPatient {

    private final ClinicDBManager dbManager;

    @GetMapping("/get-info")
    public User getPatientInfo(@RequestBody String phone) {
        return dbManager.getUserInformation(phone).orElseThrow(
                () -> new RuntimeException("User not found")
        );
    }

    @PostMapping("/change-status")
    public User changeStatus(@RequestBody VerifyBody body) {
        User user =  dbManager.getUserInformation(body.getPhone()).orElseThrow(() -> new RuntimeException("User not found"));

        user.setIdentifyCitizen(body.getCid());

        dbManager.save(user);

        return user;
    }






}
