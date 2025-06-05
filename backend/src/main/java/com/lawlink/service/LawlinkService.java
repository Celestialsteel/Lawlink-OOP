package com.lawlink.service;

import com.lawlink.entity.*;
import com.lawlink.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LawlinkService {
    @Autowired
    private TaskService taskService;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LawyerRepository lawyerRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private CaseNoteRepository caseNoteRepository;

    // ====== CASE METHODS ======
    public List<CaseEntity> getAllCases() {
        return caseRepository.findAll();
    }

    public Optional<CaseEntity> getCaseById(Long id) {
        return caseRepository.findById(id);
    }

    public CaseEntity saveCase(CaseEntity caseEntity) {
        return caseRepository.save(caseEntity);
    }

    public void deleteCase(Long id) {
        caseRepository.deleteById(id);
    }

    // ====== CLIENT METHODS ======
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    // ====== LAWYER METHODS ======
    public List<Lawyer> getAllLawyers() {
        return lawyerRepository.findAll();
    }

    public Lawyer saveLawyer(Lawyer lawyer) {
        return lawyerRepository.save(lawyer);
    }

    // ====== DOCUMENT METHODS ======
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    // ====== CASE NOTE METHODS ======
    public List<CaseNote> getAllNotes() {
        return caseNoteRepository.findAll();
    }

    public CaseNote saveNote(CaseNote note) {
        return caseNoteRepository.save(note);
    }
}
