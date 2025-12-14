package com.clibo.domain.appointment;

import com.clibo.dto.AppointmentSlot;

import java.time.LocalTime;
import java.util.List;

public class SlotTemplate {
    public static final List<AppointmentSlot> DAILY_SLOTS = List.of(
            new AppointmentSlot(LocalTime.of(8, 0), LocalTime.of(9, 30)),
            new AppointmentSlot(LocalTime.of(10, 0), LocalTime.of(11, 30)),
            new AppointmentSlot(LocalTime.of(13, 0), LocalTime.of(14, 30)),
            new AppointmentSlot(LocalTime.of(15, 0), LocalTime.of(16, 30))
    );
}
