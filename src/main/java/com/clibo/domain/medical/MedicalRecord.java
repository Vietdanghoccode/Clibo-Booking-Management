package com.clibo.domain.medical;

import com.clibo.domain.appointment.Appointment;
import com.clibo.domain.profile.Patient;
import com.clibo.domain.profile.Doctor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class MedicalRecord {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Appointment appointment;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Doctor doctor;

    private String vitalSigns;
    private String symptoms;
    private String findings;
    private String diagnosis;
    private String notes;
}
