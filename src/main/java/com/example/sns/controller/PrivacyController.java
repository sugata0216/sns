package com.example.sns.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PrivacyController {
    @GetMapping("/privacy")
    public String showPrivacy() {
        return "privacy";
    }
}
