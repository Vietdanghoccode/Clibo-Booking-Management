package com.clibo.application;

import com.clibo.domain.appointment.Appointment;
import com.clibo.domain.appointment.Department;
import com.clibo.domain.appointment.SlotTemplate;
import com.clibo.domain.profile.Doctor;
import com.clibo.domain.profile.Patient;
import com.clibo.dto.AppointmentSlot;
import com.clibo.persistence.ClinicDBManager;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/appointments/book")
@AllArgsConstructor
public class BookingAppointmentControl {
    private final ClinicDBManager clinicDBManager;

    @GetMapping("/slots")
    @ResponseBody
    public List<AppointmentSlot> findAvailableSlots(
            Long departmentId,
            LocalDate date
    ) {
        return clinicDBManager.getAvailableSlots(departmentId, date);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> confirmBooking(
            Long departmentId,
            LocalDate date,
            LocalTime startTime,
            String symptoms
    ) {
        AppointmentSlot slot = SlotTemplate.DAILY_SLOTS.stream()
                .filter(s -> s.getStart().equals(startTime))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid slot"));

        boolean available =
                clinicDBManager.checkSlotAgain(departmentId, date, slot);

        if (!available) {
            return ResponseEntity.badRequest().body("Slot not available");
        }

        // ===== DEMO: giả lập patient & department =====
        Department department = new Department();
        department.setId(departmentId);

        Patient patient = new Patient();
        patient.setId(1L); // demo: current user

        Doctor doctor = new Doctor();
        doctor.setId(1L); // demo: auto-assign

        clinicDBManager.createAppointment(
                department,
                patient,
                doctor,
                date.atTime(startTime),
                symptoms
        );

        return ResponseEntity.ok("Appointment booked");
    }
}
