package com.lawlink.controller;

import com.lawlink.dto.AppointmentRequestDTO;
import com.lawlink.entity.Appointment;
import com.lawlink.entity.Client;
import com.lawlink.entity.Lawyer;
import com.lawlink.entity.UserEntity;
import com.lawlink.repository.AppointmentRepository;
import com.lawlink.repository.ClientRepository;
import com.lawlink.repository.LawyerRepository;
import com.lawlink.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LawyerRepository lawyerRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    
    @PutMapping("/update-profile/{userId}")
    public ResponseEntity<?> updateProfile(@PathVariable Long userId, @RequestBody Client updateData) {
        Client client = clientRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        client.setLocation(updateData.getLocation());
        client.setSpecializationNeed(updateData.getSpecializationNeed());

        clientRepository.save(client);
        return ResponseEntity.ok("Client profile updated.");
    }

    
    @GetMapping("/match-lawyers/{userId}")
    public ResponseEntity<?> matchLawyers(@PathVariable Long userId) {
        Client client = clientRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        List<Lawyer> matched = lawyerRepository.findByLocationIgnoreCaseAndSpecializationIgnoreCase(
                client.getLocation(), client.getSpecializationNeed());

        return ResponseEntity.ok(matched);
    }

    
    @GetMapping("/search")
    public List<Lawyer> searchLawyers(@RequestParam String location, @RequestParam String speciality) {
        return lawyerRepository.findByLocationIgnoreCaseAndSpecializationIgnoreCase(location, speciality);
    }

    
    @PostMapping("/appointments")
    public ResponseEntity<String> createAppointment(@RequestBody AppointmentRequestDTO dto, Authentication auth) {
        String userEmail = auth.getName();

        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Client client = user.getClient();
        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        Lawyer lawyer = lawyerRepository.findById(dto.getLawyerId())
                .orElseThrow(() -> new RuntimeException("Lawyer not found"));

        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setLawyer(lawyer);
        appointment.setStatus("PENDING");

        appointmentRepository.save(appointment);
        return ResponseEntity.ok("Appointment request sent successfully.");
    }

 
    @PostMapping("/appointments/{id}/accept")
    public ResponseEntity<String> acceptAppointment(@PathVariable Long id, Authentication auth) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        String lawyerEmail = auth.getName();

        appointment.setStatus("ACCEPTED");
        appointment.setLawyerResponseMessage("Your appointment has been accepted. Please contact: " + lawyerEmail);

        appointmentRepository.save(appointment);
        return ResponseEntity.ok("Appointment accepted.");
    }

    //
    @PostMapping("/appointments/{id}/reject")
    public ResponseEntity<String> rejectAppointment(@PathVariable Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus("REJECTED");
        appointment.setLawyerResponseMessage("Unfortunately, your appointment was rejected.");
        appointmentRepository.save(appointment);

        return ResponseEntity.ok("Appointment rejected.");
    }

    
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getClientAppointments(Authentication auth) {
        String userEmail = auth.getName();
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Client client = user.getClient();
        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        List<Appointment> appointments = appointmentRepository.findByClientId(client.getId());
        return ResponseEntity.ok(appointments);
    }

    
    @GetMapping("/matched-lawyers-by-email")
    public ResponseEntity<List<Lawyer>> getMatchedLawyersByEmail(@RequestParam String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        List<Lawyer> matched = lawyerRepository.findByLocationIgnoreCaseAndSpecializationIgnoreCase(
                client.getLocation(), client.getSpecializationNeed());

        return ResponseEntity.ok(matched);
    }

   
    @GetMapping("/lawyers-by-email")
    public ResponseEntity<List<Lawyer>> getLawyersByClientEmail(@RequestParam String email) {
        Optional<Client> clientOpt = clientRepository.findByEmail(email);

        if (clientOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Client client = clientOpt.get();
        List<Lawyer> matchedLawyers = lawyerRepository.findByLocationIgnoreCaseAndSpecializationIgnoreCase(
                client.getLocation(), client.getSpecializationNeed());

        return ResponseEntity.ok(matchedLawyers);
    }
}
