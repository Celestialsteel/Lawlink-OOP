package com.lawlink.repository;

import com.lawlink.entity.Lawyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LawyerRepository extends JpaRepository<Lawyer, Long> {
    // You can add methods like: Optional<Lawyer> findByEmail(String email);
}
