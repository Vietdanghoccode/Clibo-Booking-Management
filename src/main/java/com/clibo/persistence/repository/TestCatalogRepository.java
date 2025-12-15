package com.clibo.persistence.repository;

import com.clibo.domain.medical.TestCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestCatalogRepository extends JpaRepository<TestCatalog,Long> {

}
