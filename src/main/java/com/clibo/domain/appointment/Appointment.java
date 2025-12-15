package com.clibo.domain.appointment;

import com.clibo.domain.profile.Patient;
import com.clibo.domain.profile.Doctor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Department department;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Doctor doctor;

    private LocalDateTime appointmentTime;

    private String symptoms;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    private boolean checkedIn;

    public boolean isCheckIn() {
        return checkedIn;
    }
}
