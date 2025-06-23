/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lawlink.repository;

/**
 *
 * @author barra
 */
import com.lawlink.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UserRepository extends JpaRepository<UserEntity, Long>  {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
     Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByVerificationToken(String token);
    Optional<UserEntity> findByPasswordResetToken(String token);
    
}
