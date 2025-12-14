package com.clibo.persistence.repository;

import com.clibo.domain.medical.TestRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TestRequestRepository extends JpaRepository<TestRequest, Long> {
    List<TestRequest> findByTestRequestCode(String code);
}
