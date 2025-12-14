package com.clibo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class AppointmentSlot {
    private LocalTime start;
    private LocalTime end;
}
