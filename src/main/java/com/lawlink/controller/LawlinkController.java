package com.lawlink.controller;

import com.lawlink.entity.*;
import com.lawlink.service.LawlinkService;
import com.lawlink.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LawlinkController {

    @Autowired
    private LawlinkService lawlinkService;

    @Autowired
    private TaskService taskService;

    // ===== CASE =====
    @PostMapping("/cases")
    public ResponseEntity<CaseEntity> createCase(@RequestBody CaseEntity newCase) {
        return ResponseEntity.ok(lawlinkService.saveCase(newCase));
    }

    @GetMapping("/cases")
    public ResponseEntity<List<CaseEntity>> getAllCases() {
        return ResponseEntity.ok(lawlinkService.getAllCases());
    }

    // ===== CLIENT =====
    @PostMapping("/clients")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        return ResponseEntity.ok(lawlinkService.saveClient(client));
    }

    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(lawlinkService.getAllClients());
    }

    // ===== LAWYER =====
    @PostMapping("/lawyers")
    public ResponseEntity<Lawyer> createLawyer(@RequestBody Lawyer lawyer) {
        return ResponseEntity.ok(lawlinkService.saveLawyer(lawyer));
    }

    @GetMapping("/lawyers")
    public ResponseEntity<List<Lawyer>> getAllLawyers() {
        return ResponseEntity.ok(lawlinkService.getAllLawyers());
    }

    // ===== DOCUMENT =====
    @PostMapping("/documents")
    public ResponseEntity<Document> createDocument(@RequestBody Document document) {
        return ResponseEntity.ok(lawlinkService.saveDocument(document));
    }

    @GetMapping("/documents")
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(lawlinkService.getAllDocuments());
    }

    // ===== TASK =====
    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.saveTask(task));
    }
     
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }
}
