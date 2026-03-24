package com.nguyenthanhtai.kt.controller;

import com.nguyenthanhtai.kt.entity.Doctor;
import com.nguyenthanhtai.kt.entity.Patient;
import com.nguyenthanhtai.kt.repository.PatientRepository;
import com.nguyenthanhtai.kt.service.AppointmentService;
import com.nguyenthanhtai.kt.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientRepository patientRepository;

    // GET /enroll?doctorId=1 → Hiển thị form đặt lịch
    @GetMapping("/enroll")
    public String showEnrollForm(@RequestParam Long doctorId, Model model) {
        Doctor doctor = doctorService.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ"));
        model.addAttribute("doctor", doctor);
        model.addAttribute("today", LocalDate.now().toString());
        return "enroll";
    }

    // POST /enroll → Lưu lịch hẹn
    @PostMapping("/enroll")
    public String submitEnroll(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate,
            Principal principal) {

        Doctor doctor = doctorService.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ"));

        Patient patient = patientRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bệnh nhân"));

        appointmentService.book(patient, doctor, appointmentDate);
        return "redirect:/appointments/my?success=true";
    }

    // GET /appointments/my → Lịch hẹn của tôi
    @GetMapping("/appointments/my")
    public String myAppointments(Model model, Principal principal) {
        Patient patient = patientRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bệnh nhân"));

        model.addAttribute("appointments", appointmentService.findByPatient(patient));
        return "my-appointments";
    }
}
