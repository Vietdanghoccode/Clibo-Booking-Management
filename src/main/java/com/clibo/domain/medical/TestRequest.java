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
public class TestRequest {

    @Id
    @GeneratedValue
    private Long id;

    private String testRequestCode;

    @ManyToOne
    private Appointment appointment;

    @ManyToOne
    private Doctor requestedBy;

    @ManyToOne
    private TestCatalog testCatalog;

    @Enumerated(EnumType.STRING)
    private TestStatus status;
}
