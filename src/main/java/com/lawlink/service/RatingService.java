package com.lawlink.service;

import com.lawlink.dto.RatingDTO;
import com.lawlink.entity.Client;
import com.lawlink.entity.Lawyer;
import com.lawlink.entity.Rating;
import com.lawlink.repository.ClientRepository;
import com.lawlink.repository.LawyerRepository;
import com.lawlink.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LawyerRepository lawyerRepository;

    // Saves a new rating submitted by a client for a lawyer
    public void saveRating(RatingDTO dto) {
        // Validate stars range
        if (dto.getStars() < 1 || dto.getStars() > 5) {
            throw new IllegalArgumentException("Stars must be between 1 and 5.");
        }
        // Prevent duplicate ratings
        Optional<Rating> existing = ratingRepository.findByClientIdAndLawyerId(dto.getClientId(), dto.getLawyerId());
        if (existing.isPresent()) {
            throw new IllegalStateException("You have already rated this lawyer.");
        }

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        Lawyer lawyer = lawyerRepository.findById(dto.getLawyerId())
                .orElseThrow(() -> new IllegalArgumentException("Lawyer not found"));

        Rating rating = new Rating();
        rating.setStars(dto.getStars());
        rating.setReview(dto.getReview());
        rating.setClient(client);
        rating.setLawyer(lawyer);
        rating.setSubmittedAt(LocalDateTime.now());

        ratingRepository.save(rating);
    }

    // Retrieves all ratings associated with a specific lawyer
    public List<RatingDTO> getRatingsForLawyer(Long lawyerId) {
        List<Rating> ratings = ratingRepository.findByLawyerId(lawyerId);
        return ratings.stream().map(r -> {
            RatingDTO dto = new RatingDTO();
            dto.setStars(r.getStars());
            dto.setReview(r.getReview());
            dto.setClientId(r.getClient().getId());
            dto.setLawyerId(r.getLawyer().getId());
            dto.setSubmittedAt(r.getSubmittedAt() != null ? r.getSubmittedAt().toString() : null);
            return dto;
        }).collect(Collectors.toList());
    }

    // Calculates average rating for a lawyer
    public double getAverageRating(Long lawyerId) {
        List<Rating> ratings = ratingRepository.findByLawyerId(lawyerId);
        return ratings.stream()
                .mapToInt(Rating::getStars)
                .average()
                .orElse(0.0);
    }
}