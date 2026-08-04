package com.example.sns.controller;

import com.example.sns.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PasswordResetController {
    private final UserService userService;

    public PasswordResetController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/forgot-password")
    public String showForgotPassword() {
        return "forgot-password";
    }
    @PostMapping("/forgot-password")
    public String processForgotPassword(String email, Model model) {
        userService.requestPasswordReset(email);
        // メールアドレスの存在有無に関わらず同じメッセージを表示する
        model.addAttribute("message", "入力いただいたメールアドレス宛にパスワード再設定用のリンクを送信しました。");
        return "forgot-password";
    }
    @GetMapping("/reset-password")
    public String showResetPassword(String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }
    @PostMapping("/reset-password")
    public String processResetPassword(
            String token,
            String newPassword,
            Model model) {
        String errorMessage = userService.resetPassword(token, newPassword);
        if (errorMessage != null) {
            model.addAttribute("token", token);
            model.addAttribute("errorMessage", errorMessage);
            return "reset-password";
        }
        return "redirect:/login?resetSuccess";
    }
}
