package com.clibo.application;

import com.clibo.domain.appointment.Appointment;
import com.clibo.domain.appointment.AppointmentStatus;
import com.clibo.persistence.ClinicDBManager;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/checkin")
@AllArgsConstructor
public class CheckInControl {

    private final ClinicDBManager dbManager;

    @PostMapping
    public String checkIn(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");

        Optional<Appointment> appointmentOpt = dbManager.getAppointmentInfoByPhone(phone);
        if (appointmentOpt.isEmpty()) {
            return "No upcoming appointment found";
        }

        Appointment appointment = appointmentOpt.get();
        if (appointment.isCheckedIn()) {
            return "Already checked in";
        }

        appointment.checkIn();
        appointment.setStatus(AppointmentStatus.CHECKED_IN);
        dbManager.saveAppointment(appointment);

        return "Checked in successfully";
    }
}
