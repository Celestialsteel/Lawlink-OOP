/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lawlink.controller;

/**
 *
 * @author barra
 */

import com.lawlink.entity.Lawyer;
import com.lawlink.repository.LawyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lawyer")
public class LawyerController {
    
    @Autowired
    private LawyerRepository lawyerRepository;

    
    @PutMapping("/update-profile/{userId}")
    public ResponseEntity<?> updateLawyer(@PathVariable Long userId, @RequestBody Lawyer updateData) {
        Lawyer lawyer = lawyerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Lawyer not found"));

        lawyer.setLocation(updateData.getLocation());
        lawyer.setSpecialization(updateData.getSpecialization());
        lawyer.setUniversity(updateData.getUniversity());
        lawyer.setPrice(updateData.getPrice());

        lawyerRepository.save(lawyer);
        return ResponseEntity.ok("Lawyer profile updated.");
    }
}
