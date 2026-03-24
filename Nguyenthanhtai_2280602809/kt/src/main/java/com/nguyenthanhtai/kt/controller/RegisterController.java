package com.nguyenthanhtai.kt.controller;

import com.nguyenthanhtai.kt.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final PatientService patientService;

    @GetMapping
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            Model model) {

        // Validate username trùng
        if (patientService.existsByUsername(username)) {
            model.addAttribute("error", "Tên đăng nhập đã được sử dụng!");
            return "register";
        }

        // Validate email trùng
        if (patientService.existsByEmail(email)) {
            model.addAttribute("error", "Email đã được sử dụng!");
            return "register";
        }

        // Validate username không rỗng
        if (username.trim().isEmpty() || password.trim().isEmpty() || email.trim().isEmpty()) {
            model.addAttribute("error", "Vui lòng điền đầy đủ thông tin!");
            return "register";
        }

        patientService.register(username.trim(), password, email.trim());
        return "redirect:/login?registered=true";
    }
}
