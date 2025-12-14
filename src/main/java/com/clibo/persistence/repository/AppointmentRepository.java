package com.clibo.persistence.repository;

import com.clibo.domain.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientIdAndDateAfter(Long patientId, LocalDate today);

    Optional<Appointment> findFirstByPatientPhoneOrderByDateDesc(String phone);

    List<Appointment> findByDepartmentIdAndAppointmentTimeBetween(
            Long departmentId,
            LocalDateTime start,
            LocalDateTime end
    );

    boolean existsByDepartmentIdAndAppointmentTime(
            Long departmentId,
            LocalDateTime appointmentTime
    );

}
