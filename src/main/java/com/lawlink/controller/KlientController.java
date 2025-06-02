/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lawlink.controller;

import com.lawlink.model.Klient;
import com.lawlink.repository.KlientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class KlientController {

    @Autowired
    private KlientRepository klientRepository;

    @GetMapping
    public List<Klient> getAllKlients() {
        return klientRepository.findAll();
    }

    @PostMapping
    public Klient createKlient(@RequestBody Klient klient) {
        return klientRepository.save(klient);
    }
}
