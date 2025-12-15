package com.clibo.persistence.repository;

import com.clibo.domain.auth.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findFirstByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(String phoneNumber);

    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}
