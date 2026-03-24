package com.nguyenthanhtai.kt.repository;

import com.nguyenthanhtai.kt.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Page<Doctor> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
