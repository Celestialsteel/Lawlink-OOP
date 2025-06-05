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
public class Admin extends User {
    // Polymorphism: Provides specific implementation of getRole()
    @Override
    public String getRole() {
        return "Admin";
    }

    public void verifyLawyer(Lawyer lawyer) {
        lawyer.setVerified(true);
    }
}
