package com.clibo.application;

import com.clibo.domain.appointment.Appointment;
import com.clibo.domain.profile.Doctor;
import com.clibo.domain.profile.Receptionist;
import com.clibo.domain.profile.User;
import com.clibo.dto.CheckInRequest;
import com.clibo.persistence.ClinicDBManager;
import com.clibo.security.SecurityContextUtils;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check-in")
@AllArgsConstructor
public class CheckInControl {

    private final ClinicDBManager dbManager;

    @PostMapping
    public ResponseEntity<?> checkIn(@RequestBody CheckInRequest request) {
        User recep = SecurityContextUtils.getCurrentUser();

        if (!(recep instanceof Receptionist)) {
            throw new SecurityException("Only receptionist can check in");
        }

        if (request.getAppointmentId() == null ||
                request.getPhone() == null ||
                request.getIdentifyCitizen() == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Invalid information");
        }

        try {
            // 2. Get appointment & change status
            Appointment appointment =
                    dbManager.checkInAppointment(
                            request.getAppointmentId(),
                            request.getPhone(),
                            request.getIdentifyCitizen()
                    );

            return ResponseEntity.ok(
                    "Check-in successful for appointment "
                            + appointment.getId()
            );

        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .badRequest()
                    .body("Failed to check in: " + ex.getMessage());
        }
    }

}
