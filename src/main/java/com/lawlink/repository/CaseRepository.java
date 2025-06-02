package com.lawlink.repository;

import com.lawlink.entity.CaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseRepository extends JpaRepository<CaseEntity, Long> {
    // Add custom query methods if needed
}
