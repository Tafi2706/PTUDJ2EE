package com.nguyenthanhtai.kt.service;

import com.nguyenthanhtai.kt.entity.Appointment;
import com.nguyenthanhtai.kt.entity.Doctor;
import com.nguyenthanhtai.kt.entity.Patient;
import com.nguyenthanhtai.kt.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public Appointment book(Patient patient, Doctor doctor, LocalDate date) {
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(date);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> findByPatient(Patient patient) {
        return appointmentRepository.findByPatient(patient);
    }
}
