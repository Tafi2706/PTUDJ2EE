package com.nguyenthanhtai.kt.controller;

import com.nguyenthanhtai.kt.entity.Doctor;
import com.nguyenthanhtai.kt.service.DepartmentService;
import com.nguyenthanhtai.kt.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/doctors")
@RequiredArgsConstructor
public class AdminDoctorController {

    private final DoctorService doctorService;
    private final DepartmentService departmentService;

    // Danh sách bác sĩ (ADMIN)
    @GetMapping
    public String listDoctors(Model model) {
        model.addAttribute("doctors", doctorService.findAll());
        return "admin/doctor-list";
    }

    // Form thêm mới
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        model.addAttribute("departments", departmentService.findAll());
        return "admin/doctor-form";
    }

    // Lưu bác sĩ mới
    @PostMapping("/create")
    public String createDoctor(@ModelAttribute Doctor doctor) {
        doctorService.save(doctor);
        return "redirect:/admin/doctors";
    }

    // Form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ id: " + id));
        model.addAttribute("doctor", doctor);
        model.addAttribute("departments", departmentService.findAll());
        return "admin/doctor-form";
    }

    // Lưu chỉnh sửa
    @PostMapping("/edit/{id}")
    public String updateDoctor(@PathVariable Long id, @ModelAttribute Doctor doctor) {
        doctor.setId(id);
        doctorService.save(doctor);
        return "redirect:/admin/doctors";
    }

    // Xóa bác sĩ
    @PostMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        doctorService.deleteById(id);
        return "redirect:/admin/doctors";
    }
}
