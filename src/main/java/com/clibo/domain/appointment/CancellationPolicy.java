package com.clibo.domain.appointment;

import java.time.LocalDateTime;

public class CancellationPolicy {
    public static boolean isBeforeDeadline(LocalDateTime appointmentTime) {
        return LocalDateTime.now().isBefore(appointmentTime.minusHours(24));
    }
}
