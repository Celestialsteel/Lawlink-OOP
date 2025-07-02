package com.lawlink.repository;

import com.lawlink.entity.Lawyer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface LawyerRepository extends JpaRepository<Lawyer, Long> {
 
    Optional<Lawyer> findByUserId(Long userId);
List<Lawyer> findByLocationIgnoreCaseAndSpecializationIgnoreCase(String location, String specialization);

Lawyer findByUserEmail(String email);


}
