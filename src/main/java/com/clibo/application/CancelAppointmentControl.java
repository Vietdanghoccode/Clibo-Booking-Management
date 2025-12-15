package com.clibo.application;

import com.clibo.domain.appointment.Appointment;
import com.clibo.domain.appointment.AppointmentStatus;
import com.clibo.domain.appointment.CancellationPolicy;
import com.clibo.persistence.ClinicDBManager;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/appointments/cancel")
@AllArgsConstructor
public class CancelAppointmentControl {
    private final ClinicDBManager clinicDBManager;
    private final RefundControl refundControl;

    @PostMapping("/{appointmentId}")
    public ResponseEntity<?> cancelAppointment(
            @PathVariable Long appointmentId
    ) {
        Appointment appointment = clinicDBManager.getAppointment(appointmentId);

        boolean beforeDeadline = CancellationPolicy.isBeforeDeadline(appointment.getAppointmentTime());

        if (beforeDeadline) {
            refundControl.refund(appointment);
            appointment.setStatus(AppointmentStatus.CANCELLED);
        } else {
            appointment.setStatus(AppointmentStatus.CANCELLED_NO_REFUND);
        }

        return ResponseEntity.ok("Cancel successfully");
    }


}
