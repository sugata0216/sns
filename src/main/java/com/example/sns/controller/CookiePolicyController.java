package com.example.sns.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CookiePolicyController {
    @GetMapping("/cookies")
    public String showCookiePolicy() {
        return "cookies";
    }
}
