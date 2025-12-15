package com.clibo.application;

import com.clibo.domain.appointment.Appointment;
import com.clibo.domain.medical.MedicalRecord;
import com.clibo.domain.medical.TestCatalog;
import com.clibo.domain.medical.TestRequest;
import com.clibo.domain.medical.TestResult;
import com.clibo.domain.profile.Doctor;
import com.clibo.domain.profile.User;
import com.clibo.dto.CreateTestRequestBody;
import com.clibo.dto.ExaminationInfoDTO;
import com.clibo.persistence.ClinicDBManager;
import com.clibo.security.SecurityContextUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.context.support.SecurityWebApplicationContextUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/examinations")
@AllArgsConstructor
public class ConductExaminationControl {
    private  final ClinicDBManager dbManager;

    @GetMapping("/{appointmentId}/prepare")
    public ExaminationInfoDTO prepareExaminationInfo(@PathVariable Long appointmentId) {
        User doctor = SecurityContextUtils.getCurrentUser();

        if (!(doctor instanceof Doctor)) {
            throw new SecurityException("Only doctor can conduct examination");
        }

        Appointment appointment = dbManager.getAppointment(appointmentId);

        MedicalRecord record =
                dbManager.getOrCreateMedicalRecord(
                        appointment,
                        appointment.getPatient(),
                        (Doctor) doctor
                );

        return ExaminationInfoDTO.from(appointment, record);
    }

    @PostMapping("/{medicalRecordId}/perform")
    public ResponseEntity<?> performExamination(@PathVariable Long medicalRecordId, String details) {
        MedicalRecord record = dbManager.getMedicalRecord(medicalRecordId);

        User doctor = SecurityContextUtils.getCurrentUser();

        if (!(doctor instanceof Doctor)) {
            throw new SecurityException("Only doctor can conduct examination");
        }

        if (!doctor.getId().equals(record.getDoctor().getId())) {
            throw new RuntimeException("This doctor has no permission");
        }

        record.setFindings(details);
        dbManager.save(record);

        return ResponseEntity.ok("Successfully");
     }

     @GetMapping("/order-tests")
    public List<TestCatalog> orderTests() {
        return dbManager.getTestCatalogs();
    }

    @PostMapping("/submit")
    public List<TestRequest> submit(@RequestBody CreateTestRequestBody body) {
        return dbManager.createTests(body.getTestCatalogId(), body.getAppointmentId());
    }
}
