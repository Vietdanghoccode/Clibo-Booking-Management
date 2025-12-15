package com.clibo.persistence;

import com.clibo.domain.appointment.*;
import com.clibo.domain.medical.*;
import com.clibo.domain.profile.Patient;
import com.clibo.domain.profile.Doctor;
import com.clibo.domain.profile.User;
import com.clibo.dto.AppointmentSlot;
import com.clibo.persistence.repository.*;

import com.clibo.utils.CodeGenerator;
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
    private final UserRepository userRepo;
    private final TestCatalogRepository testCatalogRepo;

    public ClinicDBManager(
            AppointmentRepository appointmentRepo,
            PatientRepository patientRepo,
            DoctorRepository doctorRepo,
            TestRequestRepository testRequestRepo,
            TestResultRepository testResultRepo,
            MedicalRecordRepository medicalRecordRepo,
            PaymentRepository paymentRepo,
            AdministratorRepository adminRepo, UserRepository userRepo, TestCatalogRepository testCatalogRepo
    ) {
        this.appointmentRepo = appointmentRepo;
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
        this.testRequestRepo = testRequestRepo;
        this.testResultRepo = testResultRepo;
        this.medicalRecordRepo = medicalRecordRepo;
        this.paymentRepo = paymentRepo;
        this.adminRepo = adminRepo;
        this.userRepo = userRepo;

        this.testCatalogRepo = testCatalogRepo;
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

    public Patient createPatient(Patient patient) {
        return patientRepo.save(patient);
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

    public void save(User user) { userRepo.save(user); }




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

        Optional<User> user = patientRepo.findByPhone(phoneNumber).map(p -> p);
        if (user.isPresent()) return user;

        user = doctorRepo.findByPhone(phoneNumber).map(d -> d);
        if (user.isPresent()) return user;

        return adminRepo.findByPhone(phoneNumber).map(a -> a);
    }

    /* =========================
       Appointment
       ========================= */
    public List<Appointment> getUpcomingAppointment(Long patientId) {
        return appointmentRepo.findByPatientIdAndAppointmentTimeAfter(patientId, LocalDate.now());
    }

    public Optional<Appointment> getAppointmentInfoByPhone(String phone) {
        return appointmentRepo.findFirstByPatientPhoneOrderByAppointmentTimeDesc(phone);
    }

    public Appointment getAppointment(Long appointmentId) {
        return appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
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

    /* =========================
       Test
       ========================= */

    public List<TestRequest> getTestRequests(String code) {
        return testRequestRepo.findByTestRequestCode(code);
    }

    public Optional<TestResult> getTestResult(Long id) {
        return testResultRepo.findById(id);
    }

    public MedicalRecord getOrCreateMedicalRecord(Appointment appointment, Patient patient, Doctor doctor) {
        return medicalRecordRepo
                .findByAppointmentId(appointment.getId())
                .orElseGet(() -> {
                    MedicalRecord record = new MedicalRecord();
                    record.setAppointment(appointment);
                    record.setPatient(patient);
                    record.setDoctor(doctor);
                    return medicalRecordRepo.save(record);
                });
    }

    public MedicalRecord getMedicalRecord(Long id) {
        return medicalRecordRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical record not found"));
    }



    public void createTestRequests(Appointment appointment, List<String> testNames) {
        for (String testName : testNames) {
            TestRequest tr = new TestRequest();
            tr.setAppointment(appointment);
            tr.setStatus(TestStatus.REQUESTED);

            testRequestRepo.save(tr);
        }
    }

    public Appointment checkInAppointment(Long appointmentId, String phone, String cid) {
        Appointment appointment = appointmentRepo.findById(appointmentId).orElseThrow(() ->
                new RuntimeException("Appointment not found"));

        if (appointment.isCheckIn()) {
            throw new RuntimeException("Appointment already checked in");
        }

        appointment.setCheckedIn(true);

        appointmentRepo.save(appointment);

        return appointment;
    }

    public Optional<MedicalRecord> getMedicalRecordByAppointment(Long appointmentId) {
        return medicalRecordRepo.findByAppointmentId(appointmentId);
    }

    public List<TestResult> getTestResultsByAppointment(Long appointmentId) {
        return testResultRepo.findAllByAppointmentId(appointmentId);
    }

    public List<TestCatalog> getTestCatalogs() {
        return testCatalogRepo.findAll(); // demo
    }

    @Transactional
    public List<TestRequest> createTests(List<Long> ids, Long appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("No appointment found"));

        List<TestCatalog> catalogs = testCatalogRepo.findAllById(ids);

        if (catalogs.size() != ids.size()) {
            throw new RuntimeException("Some test catalogs not found");
        }

        List<TestRequest> requests = new ArrayList<>();



        for (int i = 0; i < ids.size(); i++) {
            TestRequest request = new TestRequest();

            request.setTestRequestCode(CodeGenerator.generateTestRequestCode());
            request.setAppointment(appointment);
            request.setTestCatalog(catalogs.get(i));
            request.setStatus(TestStatus.REQUESTED);
        }

        testRequestRepo.saveAll(requests);

        return requests;
    }



}
