package com.lawlink.controller;
import com.lawlink.dto.RatingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.lawlink.service.RatingService;
@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitRating(@RequestBody RatingDTO dto) {
        ratingService.saveRating(dto);
        return ResponseEntity.ok("Rating submitted");
    }

    @GetMapping("/lawyer/{lawyerId}")
    public List<RatingDTO> getRatings(@PathVariable Long lawyerId) {
        return ratingService.getRatingsForLawyer(lawyerId);
    }
}
