package com.lawlink.repository;

import com.lawlink.entity.CaseNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseNoteRepository extends JpaRepository<CaseNote, Long> {
    // Optional: List<CaseNote> findByCaseId(Long caseId);
}
