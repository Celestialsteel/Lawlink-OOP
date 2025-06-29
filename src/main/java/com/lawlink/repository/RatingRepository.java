package com.lawlink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import com.lawlink.entity.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByLawyerId(Long lawyerId);
    Optional<Rating> findByClientIdAndLawyerId(Long clientId, Long lawyerId);
}
