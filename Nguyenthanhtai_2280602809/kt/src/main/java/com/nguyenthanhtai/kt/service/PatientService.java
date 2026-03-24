package com.nguyenthanhtai.kt.service;

import com.nguyenthanhtai.kt.entity.Patient;
import com.nguyenthanhtai.kt.entity.Role;
import com.nguyenthanhtai.kt.repository.PatientRepository;
import com.nguyenthanhtai.kt.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean existsByUsername(String username) {
        return patientRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return patientRepository.existsByEmail(email);
    }

    public Patient register(String username, String password, String email) {
        Role patientRole = roleRepository.findByName("PATIENT")
                .orElseThrow(() -> new RuntimeException("Role PATIENT không tồn tại"));

        Patient patient = new Patient();
        patient.setUsername(username);
        patient.setPassword(passwordEncoder.encode(password));
        patient.setEmail(email);
        patient.setRoles(Set.of(patientRole));

        return patientRepository.save(patient);
    }
}
