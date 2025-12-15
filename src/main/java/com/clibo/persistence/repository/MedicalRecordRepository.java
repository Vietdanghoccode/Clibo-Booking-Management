package com.clibo.persistence.repository;

import com.clibo.domain.appointment.Appointment;
import com.clibo.domain.medical.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatientId(Long patientId);

    Optional<MedicalRecord> findByAppointmentId(Long appointmentId);
}
