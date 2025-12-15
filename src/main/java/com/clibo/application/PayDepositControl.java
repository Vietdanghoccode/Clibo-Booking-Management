package com.clibo.application;

import com.clibo.domain.appointment.*;
import com.clibo.dto.PayDepositBody;
import com.clibo.external.IBankSystem;
import com.clibo.persistence.ClinicDBManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

@Controller
@RequestMapping("/pay-deposit")
@AllArgsConstructor
public class PayDepositControl {
    private final ClinicDBManager dbManager;
    private final IBankSystem iBankSystem;

    @GetMapping("/amount")
    public double calculateDepositAmount() {
        return 100000; // fix
    }

    @PostMapping("/confirm")
    public Payment confirmPayDeposit(PayDepositBody body) {
        Appointment appointment = dbManager.getAppointment(body.getAppointmentId());

        if (appointment == null) {
            throw new RuntimeException("Appointment not found");
        }

        boolean sended = iBankSystem.sendPaymentRequest(body.getBankInfo());

        if (!sended) {
            throw new RuntimeException("Error when send payment to bank");
        }

        appointment.setStatus(AppointmentStatus.PAYED);

        dbManager.save(appointment);

        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaymentDate(LocalDate.now());
        payment.setAmount(10000); //fix demo
        payment.setBankInfo(body.getBankInfo());
        payment.setPaymentMethod(PaymentMethod.BANK);

        return payment;


    }
}
