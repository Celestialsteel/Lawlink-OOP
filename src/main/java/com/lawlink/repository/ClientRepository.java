package com.lawlink.repository;

import com.lawlink.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    // You can add methods like: Optional<Client> findByEmail(String email);
}
