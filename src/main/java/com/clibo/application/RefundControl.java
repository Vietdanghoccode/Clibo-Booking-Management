package com.clibo.application;

import com.clibo.domain.appointment.Appointment;
import org.springframework.stereotype.Component;

@Component
public class RefundControl {
    public void refund(Appointment appointment) {
        System.out.println("Refund appointment " + appointment.getId()); // demo
    }
}
