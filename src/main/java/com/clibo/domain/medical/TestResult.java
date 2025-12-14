package com.clibo.domain.medical;

import com.clibo.domain.appointment.Appointment;
import com.clibo.domain.profile.Doctor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class TestResult {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Appointment appointment;

    @OneToOne
    private TestRequest testRequest;

    @ManyToOne
    private Doctor performedBy;

    private String resultData;

    @Enumerated(EnumType.STRING)
    private TestStatus status;
}
