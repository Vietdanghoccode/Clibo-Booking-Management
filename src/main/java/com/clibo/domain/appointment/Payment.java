package com.clibo.domain.appointment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Appointment appointment;

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private LocalDate paymentDate;
    private String bankInfo;

    private PaymentStatus status;
}
