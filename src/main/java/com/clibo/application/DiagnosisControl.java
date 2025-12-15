package com.clibo.application;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clibo.domain.appointment.Appointment;
import com.clibo.domain.appointment.AppointmentStatus;
import com.clibo.domain.medical.MedicalRecord;
import com.clibo.domain.medical.TestResult;
import com.clibo.persistence.ClinicDBManager;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/diagnosis")
@AllArgsConstructor
public class DiagnosisControl {

    private final ClinicDBManager dbManager;

    // Method to retrieve case data
    public CaseData retrieveCaseData(String phone) {
        Optional<Appointment> appointmentOpt = dbManager.getAppointmentInfoByPhone(phone);
        if (appointmentOpt.isEmpty()) {
            throw new RuntimeException("Appointment not found");
        }
        Appointment appointment = appointmentOpt.get();

        Optional<MedicalRecord> medicalRecordOpt = dbManager.getMedicalRecordByAppointment(appointment.getId());

        List<TestResult> testResults = dbManager.getTestResultsByAppointment(appointment.getId());

        return new CaseData(appointment, medicalRecordOpt.orElse(null), testResults);
    }

    @GetMapping("/case")
    public CaseData getCaseData(@RequestParam String phone) {
        return retrieveCaseData(phone);
    }

    @PostMapping("/save")
    public String saveDiagnosis(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String diagnosis = request.get("diagnosis");
        String notes = request.get("notes");

        Optional<Appointment> appointmentOpt = dbManager.getAppointmentInfoByPhone(phone);
        if (appointmentOpt.isEmpty()) {
            return "Appointment not found";
        }
        Appointment appointment = appointmentOpt.get();

        Optional<MedicalRecord> medicalRecordOpt = dbManager.getMedicalRecordByAppointment(appointment.getId());
        if (medicalRecordOpt.isEmpty()) {
            return "Medical record not found";
        }
        MedicalRecord medicalRecord = medicalRecordOpt.get();
        medicalRecord.setDiagnosis(diagnosis);
        medicalRecord.setNotes(notes);
        dbManager.save(medicalRecord);

        appointment.setStatus(AppointmentStatus.COMPLETED);
        dbManager.saveAppointment(appointment);

        return "Diagnosis saved and appointment completed";
    }

    // DTO for case data
    public static class CaseData {
        public Appointment appointment;
        public MedicalRecord medicalRecord;
        public List<TestResult> testResults;

        public CaseData(Appointment appointment, MedicalRecord medicalRecord, List<TestResult> testResults) {
            this.appointment = appointment;
            this.medicalRecord = medicalRecord;
            this.testResults = testResults;
        }
    }
}
