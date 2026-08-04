package com.example.sns.controller;

import com.example.sns.entity.Notification;
import com.example.sns.security.CustomUserDetails;
import com.example.sns.service.NotificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @GetMapping("/notifications")
    public String notifications(
            @AuthenticationPrincipal CustomUserDetails loginUser,
            Model model) {
        // 既読にする
        notificationService.markAllAsRead(loginUser.getUser().getUserId());
        model.addAttribute(
                "notifications",
                notificationService.findByReceiverId(
                        loginUser.getUser().getUserId()));
        return "notifications";
    }
}
