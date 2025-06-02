package com.lawlink.repository;

import com.lawlink.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    // Optional: List<Document> findByCaseId(Long caseId);
}
