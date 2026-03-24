package com.nguyenthanhtai.kt.controller;

import com.nguyenthanhtai.kt.entity.Doctor;
import com.nguyenthanhtai.kt.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final DoctorService doctorService;

    @GetMapping({"/", "/home", "/courses"})
    public String home(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String keyword) {

        int pageSize = 5;
        Page<Doctor> doctorPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            doctorPage = doctorService.searchDoctors(keyword.trim(), page, pageSize);
        } else {
            doctorPage = doctorService.getDoctors(page, pageSize);
        }

        model.addAttribute("doctorPage", doctorPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", doctorPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "home";
    }
}
