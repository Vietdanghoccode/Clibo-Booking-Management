package com.clibo.persistence;

import com.clibo.domain.appointment.*;
import com.clibo.domain.medical.MedicalRecord;
import com.clibo.domain.profile.Patient;
import com.clibo.domain.profile.Doctor;
import com.clibo.domain.medical.TestRequest;
import com.clibo.domain.medical.TestResult;
import com.clibo.domain.profile.User;
import com.clibo.dto.AppointmentSlot;
import com.clibo.persistence.repository.*;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.metamodel.internal.AbstractDynamicMapInstantiator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ClinicDBManager {

    private final AppointmentRepository appointmentRepo;
    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;
    private final TestRequestRepository testRequestRepo;
    private final TestResultRepository testResultRepo;
    private final MedicalRecordRepository medicalRecordRepo;
    private final PaymentRepository paymentRepo;
    private final AdministratorRepository adminRepo;

    public ClinicDBManager(
            AppointmentRepository appointmentRepo,
            PatientRepository patientRepo,
            DoctorRepository doctorRepo,
            TestRequestRepository testRequestRepo,
            TestResultRepository testResultRepo,
            MedicalRecordRepository medicalRecordRepo,
            PaymentRepository paymentRepo,
            AdministratorRepository adminRepo
    ) {
        this.appointmentRepo = appointmentRepo;
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
        this.testRequestRepo = testRequestRepo;
        this.testResultRepo = testResultRepo;
        this.medicalRecordRepo = medicalRecordRepo;
        this.paymentRepo = paymentRepo;
        this.adminRepo = adminRepo;
    }

    public Optional<User> findUserByPhone(String phoneNumber) {

        Optional<? extends User> user;

        user = patientRepo.findByPhone(phoneNumber);
        if (user.isPresent()) return Optional.of(user.get());

        user = doctorRepo.findByPhone(phoneNumber);
        if (user.isPresent()) return Optional.of(user.get());

        user = adminRepo.findByPhone(phoneNumber);
        if (user.isPresent()) return Optional.of(user.get());

        return Optional.empty();
    }

    public Appointment save(Appointment appointment) {
        return appointmentRepo.save(appointment);
    }
    
    public MedicalRecord save(MedicalRecord record) {
        return medicalRecordRepo.save(record);
    }
    
    public TestResult save(TestResult result) {
        return testResultRepo.save(result);
    }

    public Patient save(Patient patient) {
        return patientRepo.save(patient);
    }




    /* =========================
       User
       ========================= */

    public Optional<User> getUserInformation(Long userId) {

        Optional<? extends User> user;

        user = patientRepo.findById(userId);
        if (user.isPresent()) return Optional.of(user.get());

        user = doctorRepo.findById(userId);
        if (user.isPresent()) return Optional.of(user.get());

        user = adminRepo.findById(userId);
        if (user.isPresent()) return Optional.of(user.get());

        return Optional.empty();
    }

    public Optional<User> getUserInformation(String phoneNumber) {

        Optional<? extends User> user;

        user = patientRepo.findByPhone(phoneNumber);
        if (user.isPresent()) return Optional.of(user.get());

        user = doctorRepo.findByPhone(phoneNumber);
        if (user.isPresent()) return Optional.of(user.get());

        user = adminRepo.findByPhone(phoneNumber);
        if (user.isPresent()) return Optional.of(user.get());

        return Optional.empty();
    }

    /* =========================
       Appointment
       ========================= */
    public List<Appointment> getUpcomingAppointment(Long patientId) {
        return appointmentRepo.findByPatientIdAndDateAfter(patientId, LocalDate.now());
    }

    public Optional<Appointment> getAppointmentInfoByPhone(String phone) {
        return appointmentRepo.findFirstByPatientPhoneOrderByDateDesc(phone);
    }

    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepo.save(appointment);
    }

    /* =========================
       Slot / Availability
       ========================= */
    public List<AppointmentSlot> getAvailableSlots(Long departmentId, LocalDate date) {
        LocalDateTime startDay = date.atTime(0, 0);
        LocalDateTime endDay = date.atTime(23, 59);


        // Các appointment đã tồn tại trong department + ngày đó
        List<Appointment> booked =
                appointmentRepo.findByDepartmentIdAndAppointmentTimeBetween(
                        departmentId, startDay, endDay
                );

        Set<LocalTime> occupiedStarts = booked.stream()
                .map(a -> a.getAppointmentTime().toLocalTime())
                .collect(Collectors.toSet());

        List<AppointmentSlot> available = new ArrayList<>();

        for (AppointmentSlot slot : SlotTemplate.DAILY_SLOTS) {
            if (!occupiedStarts.contains(slot.getStart())) {
                available.add(slot);
            }
        }

        return available;
    }

    public boolean checkSlotAgain(
            Long departmentId,
            LocalDate date,
            AppointmentSlot slot
    ) {
        return !appointmentRepo.existsByDepartmentIdAndAppointmentTime(
                departmentId,
                date.atTime(slot.getStart())
        );
    }

    /* ===== BOOK ===== */

    public Appointment createAppointment(
            Department department,
            Patient patient,
            Doctor doctor,
            LocalDateTime time,
            String symptoms
    ) {
        Appointment a = new Appointment();
        a.setDepartment(department);
        a.setPatient(patient);
        a.setDoctor(doctor);
        a.setAppointmentTime(time);
        a.setSymptoms(symptoms);
        a.setStatus(AppointmentStatus.PENDING_DEPOSIT);
        return appointmentRepo.save(a);
    }

    /* =========================
       Medical
       ========================= */

    public List<MedicalRecord> getMedicalHistory(Long patientId) {
        return medicalRecordRepo.findByPatientId(patientId);
    }

    public Optional<MedicalRecord> getMedicalRecord(Long recordId) {
        return medicalRecordRepo.findById(recordId);
    }

    public Optional<MedicalRecord> getMedicalRecordByAppointment(Long appointmentId) {
        return medicalRecordRepo.findByAppointment_Id(appointmentId);
    }

    /* =========================
       Test
       ========================= */

    public List<TestRequest> getTestRequests(String code) {
        return testRequestRepo.findByTestRequestCode(code);
    }

    public Optional<TestResult> getTestResult(Long id) {
        return testResultRepo.findById(id);
    }

    public List<TestResult> getTestResultsByAppointment(Long appointmentId) {
        return testResultRepo.findByAppointmentId(appointmentId);
    }
}
