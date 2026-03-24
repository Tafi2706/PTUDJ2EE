package com.nguyenthanhtai.kt.service;

import com.nguyenthanhtai.kt.entity.Doctor;
import com.nguyenthanhtai.kt.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public Page<Doctor> getDoctors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return doctorRepository.findAll(pageable);
    }

    public Page<Doctor> searchDoctors(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return doctorRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public Optional<Doctor> findById(Long id) {
        return doctorRepository.findById(id);
    }

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public void deleteById(Long id) {
        doctorRepository.deleteById(id);
    }

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }
}
