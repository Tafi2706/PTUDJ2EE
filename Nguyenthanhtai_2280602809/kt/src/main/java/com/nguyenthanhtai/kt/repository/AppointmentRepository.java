package com.nguyenthanhtai.kt.repository;

import com.nguyenthanhtai.kt.entity.Appointment;
import com.nguyenthanhtai.kt.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatient(Patient patient);
}
