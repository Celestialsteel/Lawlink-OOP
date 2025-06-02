/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lawlink.model;

import jakarta.persistence.*;
import java.util.*;

/**
 *
 * @author barra
 */
@Entity
public class Document {
    // Encapsulation
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String docId;

    private String name;
    private String version;

    @ManyToOne
    private LegalCase relatedCase;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LegalCase getRelatedCase() {
        return relatedCase;
    }

    public void setRelatedCase(LegalCase relatedCase) {
        this.relatedCase = relatedCase;
    }
}
