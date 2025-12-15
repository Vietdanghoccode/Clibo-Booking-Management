package com.clibo.dto;

import com.clibo.domain.appointment.Appointment;
import com.clibo.domain.medical.MedicalRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExaminationInfoDTO {
    private Long appointentId;
    private String patientName;
    private String symptoms;
    private String diagnosis;

    public static ExaminationInfoDTO from(
            Appointment a,
            MedicalRecord r
    ) {
        return new ExaminationInfoDTO(
                a.getId(),
                a.getPatient().getFullName(),
                r.getSymptoms(),
                r.getDiagnosis()
        );
    }
}
