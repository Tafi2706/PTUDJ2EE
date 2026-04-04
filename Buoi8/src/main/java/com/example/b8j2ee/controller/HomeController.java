package com.example.b8j2ee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String home() {
        return "redirect:/products";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "error/403";
    }
}
