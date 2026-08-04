package com.example.sns.controller;

import com.example.sns.entity.User;
import com.example.sns.form.SignupForm;
import com.example.sns.service.MailService;
import com.example.sns.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class SignupController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public SignupController(UserService userService, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @GetMapping("/signup")
    public String showSignup(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "signup";
    }
    @PostMapping("/signup")
    public String signup(
            @ModelAttribute SignupForm signupForm,
            Model model) {
        User user = new User();
        user.setUsername(signupForm.getUsername());
        user.setEmail(signupForm.getEmail());
        // パスワードは暗号化して保存
        user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
        user.setBio(signupForm.getBio());
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        String errorMessage = userService.register(user);
        if (errorMessage != null) {
            model.addAttribute("signupForm", signupForm);
            model.addAttribute("errorMessage", errorMessage);
            return "signup";
        }
        mailService.sendVerificationMail(user);
        return "signup-complete";
    }
    @GetMapping("/verify")
    public String verify(String token) {
        userService.verify(token);
        return "signup-complete";
    }
}
