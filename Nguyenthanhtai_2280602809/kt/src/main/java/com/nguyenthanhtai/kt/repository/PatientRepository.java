package com.nguyenthanhtai.kt.repository;

import com.nguyenthanhtai.kt.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
